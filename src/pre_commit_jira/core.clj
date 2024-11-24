(ns pre-commit-jira.core
  (:require [pre-commit-jira.hooks.add-jira-ticket :refer [add-jira-ticket]])
  (:gen-class))

(defn -main
  [& args]
  (let [commit-msg-file-path (first args)]
    (add-jira-ticket commit-msg-file-path))
  (System/exit 1))

; Left on error while building docker image