(ns pre-commit-jira.hooks.add-jira-ticket
  (:require
    [clojure.java.shell :as shell]
    [clojure.string     :as str]
    [clojure.test       :refer [is with-test]]))

(defn get-current-branch-name
  []
  (-> (shell/sh "git" "symbolic-ref" "--short" "HEAD")
      (:out)
      (str/replace "\n" "")))

(with-test
  (defn get-jira-ticket-from
    [string]
    (let [ticket-pattern #"[A-Z]+-\d+"] (re-find ticket-pattern string)))
  (is (= "TICKET-123" (get-jira-ticket-from "feature/TICKET-123-some-feature")))
  (is (= nil (get-jira-ticket-from "feature/some-feature"))))

(with-test
  (defn special-commit?
    [commit-msg]
    (let [is-merge-commit (str/starts-with? commit-msg "Merge ")]
      is-merge-commit))
  (is (= true (special-commit? "Merge pull request")))
  (is (= true
         (special-commit?
           "Merge branch 'develop' into feature/TICKET-123-some-feature"))))

(with-test
  (defn prepend-jira-ticket
    "Prepends Jira ticket based on Conventional Commits specification.
     Doesn't check whether `commit-msg` has ticket already.  
     Format: `<type>: <ticket> <description>`  
     Example: `feat: TICKET-123 simple solution for a big trouble`"
    [commit-msg ticket]
    (let [[commit-type description] (mapv str/trim
                                      (str/split commit-msg #":" 2))]
      (str commit-type ": " ticket " " description)))
  (is (= "feat: TICKET-123 simple solution for a big trouble"
         (prepend-jira-ticket "feat: simple solution for a big trouble"
                              "TICKET-123")))
  (is (= "feat: TICKET-123 commit\nOptional body\nOptional footer"
         (prepend-jira-ticket "feat: commit\nOptional body\nOptional footer"
                              "TICKET-123"))
      "should correctly handle commit message with body and footer"))

(defn add-jira-ticket
  [commit-msg-file-path]
  (let [commit-msg             (slurp commit-msg-file-path)
        branch-name            (get-current-branch-name)
        ticket-from-commit-msg (get-jira-ticket-from commit-msg)
        ticket-from-branch     (get-jira-ticket-from branch-name)]
    ; TODO: handle case when ticket in branch name is different from ticket
    ; in commit-msg
    (when (and (not ticket-from-commit-msg)
               ticket-from-branch
               (not (special-commit? commit-msg)))
      (->> (prepend-jira-ticket commit-msg ticket-from-branch)
           (spit commit-msg-file-path)))))
