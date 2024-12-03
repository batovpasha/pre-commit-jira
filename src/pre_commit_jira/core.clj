(ns pre-commit-jira.core
  (:require [pre-commit-jira.hooks.add-jira-ticket :refer [add-jira-ticket]])
  (:gen-class))

(defn -main
  [& args]
  (let [[hook commit-msg-file-path] args]
    (case hook
      "add-jira-ticket" (add-jira-ticket commit-msg-file-path)))
  (System/exit 0))
