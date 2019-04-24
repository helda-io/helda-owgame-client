(ns helda-owgame-client.maps
  (:require
    [helda-owgame-client.rest.entities :as client]
    )
  )

(defn render-background-layer [entity]
  (-> entity :attrs :tiles)
  )

(defn render-objects-layer [entity]
  (-> entity :attrs :legend)
  )

(defn convert-room-map-entity [entity]{
  :room-id (-> entity :attrs :name)
  :world-id (:world entity)
  :layers [
    (render-background-layer entity)
    (render-objects-layer entity)
    ]
  })

(defn load-map [world-id room-id]
  (some->
    (client/find-entities world-id ["helda.RoomMap"] [room-id])
    first
    convert-room-map-entity
    )
  )
