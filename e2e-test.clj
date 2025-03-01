#!/usr/bin/env bb

(require '[babashka.process :refer [sh shell]] '[clojure.string :as str])

(def is-pre-commit-installed (:exit (sh "command -v pre-commit")))
(when-not is-pre-commit-installed
  (println (str "\033[0;31m"
                "Please, install pre-commit to run e2e tests: "
                "https://pre-commit.com/"))
  (System/exit 1))

(def pre-commit-jira-version "v1.0.0")
(def tmp-test-dir "/tmp/pre-commit-jira-test")

(shell "mkdir" "-p" tmp-test-dir)

(shell "git config --global user.email"
       "batovpasha@gmail.com"
       {:dir tmp-test-dir})
(shell "git config --global user.name" "Pavlo Batov" {:dir tmp-test-dir})
(shell "git init" {:dir tmp-test-dir})

(def pre-commit-config-path (str tmp-test-dir "/.pre-commit-config.yaml"))
(def pre-commit-config
  (-> (str "repos:\n"
           "  - repo: https://github.com/batovpasha/pre-commit-jira\n"
             "    rev: {{pre-commit-jira-version}}\n"
           "    hooks:\n" "      -id: add-jira-ticket\n")
      (str/replace "{{pre-commit-jira-version}}" pre-commit-jira-version)))
(spit pre-commit-config-path pre-commit-config)
(shell "pre-commit install --hook-type commit-msg" {:dir tmp-test-dir})

;; TODO
;; (deftest add-jira-ticket
;;   ())

;; (t/deftest addition
;;   (t/is (= 1 2)))

;; (def test-results

;; (let [{:keys [fail error]} test-results]
;;   (when (pos? (+ fail error))
;;     (System/exit 1)))
