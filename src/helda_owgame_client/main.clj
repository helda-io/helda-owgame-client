(ns helda-owgame-client.main
  (:require
    [org.httpkit.server :as server]
    [helda-owgame-client.handler :as handler]
    [helda-owgame-client.system :as system]
    )
  (:gen-class)
  )

(defn -main [& [port]]
  (let [port (or port 3005)]
    (server/run-server
      (handler/create
        (system/new-system)
        )
      {:port port}
      )
    )
  )
