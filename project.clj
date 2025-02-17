(defproject pre-commit-jira "1.0.0-SNAPSHOT"
  :description  "Collection of pre-commit hooks for JIRA workflow"
  :url          "https://github.com/batovpasha/pre-commit-jira"
  :license      {:name "MIT", :distribution :repo}
  :dependencies [[org.clojure/clojure "1.12.0"]]
  :plugins      [[lein-zprint "1.2.9"]
                 [com.github.clj-kondo/lein-clj-kondo "2024.11.14"]
                 [lein-cloverage "1.2.2"]]
  :test-paths   ["test" "src"] ; src folder contains inline tests
                               ; written using "with-test" macro
  :cloverage    {:junit?         true,
                 :exclude-call   [clojure.test/is],
                 :fail-threshold 95}
  :zprint       {:old?          false,
                 :style         [:justified-original
                                 :sort-require
                                 :ns-justify
                                 :sort-dependencies],
                 :parse-string? true,
                 :fn-map        {"are" [:none {:style :areguide-nl}]}}
  :main         ^:skip-aot pre-commit-jira.core
  :target-path  "target/%s"
  :profiles     {:uberjar {:aot :all,
                           :jvm-opts
                             ["-Dclojure.compiler.direct-linking=true"]}})
