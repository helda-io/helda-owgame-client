(ns helda-owgame-client.maps
  (:require
    [clojure.set :refer [map-invert]]
    [clojure.string :refer [join split]]
    [helda-owgame-client.rest.entities :as client]
    )
  )

(def tiles-atom (atom nil))

(def tiles-world-id "owgame1")

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

(defn find-tile-code [id]
  (if id
    (some-> @tiles-atom id :tile-code)
    )
  )

(defn find-tile-code-or-default [id default-tile]
  (or
    (find-tile-code id)
    (find-tile-code default-tile)
    )
  )

(defn find-comp [comp-tag]
  (client/find-entities tiles-world-id ["helda.TileSet"] [comp-tag])
  )

(defn render-background-layer [entity]
  (let [
    legend (-> entity :attrs :legend map-invert)
    ]
    (as-> entity v
      (:attrs v)
      (:tiles v)
      (join " " v)
      (split v #" ")
      (map #(get legend %) v)
      (map #(find-tile-code-or-default % :green) v)
      )
    )
  )

(def map-size 17)

(defn init-map []
  (repeat (* map-size map-size) 0)
  )

(defn merge-tile [tile1 tile2]
  (if (= tile1 0)
    tile2
    tile1
    )
  )

(defn merge-map [map1 map2]
  (map merge-tile map1 map2)
  )

;first parameter is geo-object
(defn init-geo-object [geo comp]
  (let [
    tiles (:tiles comp)
    empty-map (init-map)
    ]
    (reduce merge-map empty-map
      (for [
        i (range (:w geo))
        j (range (:h geo))
        ]
        (let [
          x (+ i (:x geo))
          y (+ j (:y geo))
          c (+ x (* y map-size))
          ]
          (assoc empty-map c (-> tiles (get j) (get i)))
          )
        )
      )
    )
  )

(defn map-geo[geo-list]
  (map
    init-geo-object
    geo-list
    (map #(find-comp (:tile-id %)) geo-list)
    )
  )

(defn render-objects-layer [entity]
  (let [tile-map (init-map)]
    (->> entity :attrs :geo-objects
      map-geo
      (reduce merge-map (init-map))
      )
    )
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
