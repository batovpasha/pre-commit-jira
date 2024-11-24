(ns pre-commit-jira.hooks.add-jira-ticket
  (:require [clojure.java.shell :as shell]))

(defn get-current-branch-name
  []
  (let [{out :out} (shell/sh "git" "symbolic-ref" "--short" "HEAD")]
    out))

(defn add-jira-ticket
  [commit-msg-file-path]
  (let [commit-msg (slurp commit-msg-file-path)
        branch-name (get-current-branch-name)]
    (println "commit:" commit-msg)
    (println "branch:" branch-name)))
