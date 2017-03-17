(defproject plan "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [me.raynes/fs "1.4.6"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/core.unify "0.5.7"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}})

