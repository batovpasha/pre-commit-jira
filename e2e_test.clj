#!/usr/bin/env bb

(require '[babashka.process :refer [sh shell *defaults*]]
         '[clojure.string :as str]
         '[babashka.fs :as fs]
         '[clojure.test :refer [use-fixtures deftest is run-tests]])

(def is-pre-commit-installed
  (-> (sh "which" "pre-commit")
      :exit
      zero?))
(when-not is-pre-commit-installed
  (println (str "\033[0;31m"
                "Please, install pre-commit to run e2e tests: "
                "https://pre-commit.com/"))
  (System/exit 1))

(def pre-commit-jira-version "v1.0.0")
(def tmp-test-dir "/tmp/pre-commit-jira-test")

(defn setup-git-repo
  []
  (shell "git init")
  (shell "git config user.email" "batovpasha@gmail.com")
  (shell "git config user.name" "Pavlo Batov"))

(def pre-commit-config-template
  "
repos:
  - repo: https://github.com/batovpasha/pre-commit-jira
    rev: {{pre-commit-jira-version}}
    hooks:
      - id: add-jira-ticket
")
(defn setup-pre-commit
  []
  (let [pre-commit-config-path (str (fs/path tmp-test-dir
                                             ".pre-commit-config.yaml"))
        pre-commit-config      (-> pre-commit-config-template
                                   (str/replace "{{pre-commit-jira-version}}"
                                                pre-commit-jira-version))]
    (spit pre-commit-config-path pre-commit-config)
    (shell "pre-commit install --hook-type commit-msg")))

(defn setup [] (setup-git-repo) (setup-pre-commit))

(use-fixtures :once
              (fn [f]
                (shell "mkdir" "-p" tmp-test-dir)
                (binding [*defaults* (merge *defaults* {:dir tmp-test-dir})]
                  (setup)
                  (f))
                (shell "rm" "-rf" tmp-test-dir)))

(deftest add-jira-ticket
  (shell "git checkout" "-b" "feature/TICKET-123-some-feature")
  (shell "git commit --allow-empty" "-m" "feat: add new feature")
  (let [expected (-> (shell {:out :string} "git --no-pager log -1 --pretty=%B")
                     :out
                     str/split-lines
                     first)]
    (is (= "feat: TICKET-123 add new feature" expected)
        "should prepend JIRA ticket from branch to commit message")))

(let [{:keys [fail error]} (run-tests)]
  (when (pos? (+ fail error)) (System/exit 1)))
