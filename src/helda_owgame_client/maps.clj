(ns helda-owgame-client.maps
  (:require
    [helda-owgame-client.rest.entities :as client]
    )
  )

(defn load-map [world-id room-id]
  (client/find-entities world-id ["helda.RoomMap"] [room-id])
  )
