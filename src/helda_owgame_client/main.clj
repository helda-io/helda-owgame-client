(ns helda-owgame-client.main
  (:require [com.stuartsierra.component :as component]
            [reloaded.repl :refer [set-init! go]])
  (:gen-class))

(defn -main [& [port]]
  (let [port (or port 3000)]
    (require 'helda-owgame-client.system)
    (set-init! #((resolve 'helda-owgame-client.system/new-system) {:http {:port port}}))
    (go)))
