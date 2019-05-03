(defproject helda-owgame-client "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [
    [org.clojure/clojure "1.8.0"]
    [http-kit "2.1.19"]
    [metosin/kekkonen "0.3.2"]
    [environ "1.0.0"]
    ]

  :main helda-owgame-client.main

  :profiles {
    :uberjar {
      :aot [helda-owgame-client.main]
      :main helda-owgame-client.main
      :uberjar-name "helda-owgame-client.jar"
      }
    }
  )
