(defproject Turtle "0.1.0-SNAPSHOT"
  :description "Turtle Graphics Processor"
  :main turtle-graphics.core
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [quil "4.0.0-SNAPSHOT"]
                 [org.clojure/core.async "1.6.673"]
                 [org.clojure/tools.namespace "1.3.0"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]}}
  :plugins [[speclj "3.3.2"]]
  :test-paths ["spec"])
