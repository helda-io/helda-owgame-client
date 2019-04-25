(ns helda-owgame-client.maps
  (:require
    [helda-owgame-client.rest.entities :as client]
    )
  )

(def tiles-atom (atom nil))

(defn load-tiles[world-id]
  (if-let [entities (client/find-entities world-id ["helda.SingleTile"])]
    (reset! tiles-atom
      (zipmap
        (map #(-> % :attrs :comp-id keyword) entities)
        (map #(:attrs %) entities)
        )
      )
    )
  )

(defn find-tile-code [id] (some-> @tiles-atom id :tile-code))

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
