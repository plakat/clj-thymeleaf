(defproject clj-thymeleaf "0.1.0-SNAPSHOT"
  :description "Clojure integration for Thymeleaf templating"
  :url "https://github.com/plakat/clj-thymeleaf"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [org.thymeleaf/thymeleaf "2.1.3.RELEASE"]
                 [org.slf4j/slf4j-log4j12 "1.7.7"]
                 ]
  :aot :all
  :javac-options ["-target" "1.7" "-source" "1.7"]
  :scm {:name "git"
        :url "https://github.com/plakat/clj-thymeleaf"}
  )
