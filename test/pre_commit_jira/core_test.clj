(ns pre-commit-jira.core-test
  (:require
    [clojure.java.shell   :as shell]
    [clojure.test         :refer :all]
    [pre-commit-jira.core :refer [-main]]))

(deftest add-jira-ticket-hook
  (are [test-name commit-msg branch-name expected]
    (let [commit-file-name "commit-msg.txt"
          spit-calls       (atom [])]
      (with-redefs [slurp    (fn [& _] commit-msg)
                    shell/sh (fn [& _] {:out (str branch-name "\n")})
                    spit     (fn [& args] (swap! spit-calls conj args) nil)]
        (-main "add-jira-ticket" commit-file-name)
        (is (= @spit-calls expected) test-name)))
    "should prepend JIRA ticket from branch to commit message"
      "feat: add new feature"
      "feature/TICKET-123-some-feature"
      ['("commit-msg.txt" "feat: TICKET-123 add new feature")]
    "should not prepend JIRA ticket when the commit message already contains it"
      "feat: TICKET-123 add new feature"
      "feature/TICKET-123-some-feature"
                                                                                                                                                                                 []
    "should not prepend JIRA ticket when there is no ticket in a branch name"
      "feat: add new feature"
      "feature/some-feature"
                                                                                                                                                                                 []
    "should not prepend JIRA ticket when commit is a merge commit"
      "Merge branch 'develop' into feature/TICKET-123-some-feature"
      "develop"
                                                                                                                                                                                 []))
