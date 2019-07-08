(defproject elfeed-cljsrn "0.1.0-SNAPSHOT"
  :description "Elfeed mobile client built with clojurescript and react native"
  :url "http://github.com/areina/elfeed-cljsrn"
  :license {:name "Apache License Version 2.0"
            :url  "http://www.apache.org/licenses/LICENSE-2.0.html"}
  ; :dependencies [[org.clojure/clojure "1.9.0-alpha11"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.9.293"]
                 [reagent "0.6.0" :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server]]
                 [re-frame "0.9.1"]
                 [day8.re-frame/async-flow-fx "0.0.6"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [lein-doo "0.1.7"]]
  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-doo "0.1.7"]]
  :clean-targets ["target/" "index.ios.js" "index.android.js"]
  :aliases {"prod-build" ^{:doc "Recompile code with prod profile."}
            ["do" "clean"
             ;; NO IOS VERSION AT THIS MOMENT
             ;; ["with-profile" "prod" "cljsbuild" "once" "ios"]
             ["with-profile" "prod" "cljsbuild" "once" "android"]]}
  :profiles
  {:dev
   {:dependencies [[figwheel-sidecar "0.5.8"]
                    [cider/piggieback "0.4.1"]]
                  ;;  [com.cemerick/piggieback "0.2.1"]]
    :plugins [[lein-figwheel "0.5.8"]]
    :source-paths ["src" "env/dev"]
    :cljsbuild    {:test-commands {"test" ["lein" "doo" "node" "test" "once"]}
                   :builds [{:id "ios"
                             :source-paths ["src" "env/dev"]
                             :figwheel true
                             :compiler {:output-to     "target/ios/not-used.js"
                                        :main          "env.ios.main"
                                        :output-dir    "target/ios"
                                        :optimizations :none}}
                            {:id "android"
                             :source-paths ["src" "env/dev"]
                             :figwheel true
                             :compiler {:main          "env.android.main"
                                        :output-to     "target/android/not-used.js"
                                        :output-dir    "target/android"
                                        :verbose true
                                        :optimizations :none}}
                            {:id "test"
                             :source-paths ["src" "test" "env/test"]
                             :compiler {:main runners.doo
                                        :preloads ['env.all]
                                        :optimizations :none
                                        :target :nodejs
                                        :output-to "target/test/all-tests.js"
                                        :output-dir "target/test"}}]}
    ; :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
    :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}
   :prod {:cljsbuild {:builds [{:id "ios"
                                :source-paths ["src" "env/prod"]
                                :compiler     {:output-to     "index.ios.js"
                                               :main          "env.ios.main"
                                               :output-dir    "target/ios"
                                               :static-fns    true
                                               :optimize-constants true
                                               :optimizations :simple
                                               :closure-defines {"goog.DEBUG" false}}}
                               {:id "android"
                                :source-paths ["src" "env/prod"]
                                :compiler     {:output-to     "index.android.js"
                                               :main          "env.android.main"
                                               :output-dir    "target/android"
                                               :static-fns    true
                                               :optimize-constants true
                                               :optimizations :simple
                                               :closure-defines {"goog.DEBUG" false}}}]}}})
