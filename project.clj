(defproject pre-commit-jira "0.1.0-SNAPSHOT"
  :description "pre-commit hooks for Jira workflow"
  :url "https://github.com/batovpasha/pre-commit-jira"
  :license {:name "MIT"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main ^:skip-aot pre-commit-jira.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
