(ns helda-owgame-client.maps
  (:require
    [clojure.set :refer [map-invert]]
    [clojure.string :refer [join split]]
    [helda-owgame-client.rest.entities :as client]
    )
  )

;todo implement validation  

(def tiles-atom (atom nil))
(def foreground-tiles-atom (atom nil))

(def tiles-world-id "owgame1")

(defn fill-dict [entities]
  (zipmap
    (map #(-> % :attrs :compId keyword) entities)
    (map #(:attrs %) entities)
    )
  )

(defn load-tiles[world-id]
  (if-let [entities (client/find-entities world-id ["helda.SingleTile"])]
    (reset! tiles-atom (fill-dict entities))
    )
  (if-let [entities (client/find-entities world-id ["helda.ForegroundTile"])]
    (reset! foreground-tiles-atom (fill-dict entities))
    )
  )

(defn find-tile [id]
  (if-not @tiles-atom (load-tiles tiles-world-id))
  (if id (id @tiles-atom))
  )

(defn find-tile-or-default [id default-tile]
  (or
    (find-tile id)
    (find-tile default-tile)
    )
  )

(defn find-simple-comp [comp-tag]
  (first
    (client/find-entities tiles-world-id ["helda.TileSet"] [comp-tag])
    )
  )

(defn find-scalable-comp [comp-tag]
  (first
    (client/find-entities tiles-world-id ["helda.ScalableTileSet"] [comp-tag])
    )
  )

(defn find-comp [geo-object]
  (let [tile-id (:tile-id geo-object)]
    (if (:tiles geo-object)
      (find-scalable-comp tile-id)
      (find-simple-comp tile-id)
      )
    )
  )

(def map-size 17)

(defn init-map []
  (vec (repeat map-size
    (vec (repeat map-size nil))
    ))
  )

(def empty-map (init-map))

(defn merge-tile [tile1 tile2] (or tile1 tile2))

(defn merge-map [map1 map2]
  (map
    #(map merge-tile %1 %2)
    map1 map2
    )
  )

;returns [[tile]]
(defn lookup-tiles [geo comp]
  (cond
    (:tiles geo)
      (mapv
        #(mapv (fn [tile] (get-in comp [:attrs (keyword tile)])) %)
        (:tiles geo)
        )
    :else (-> comp :attrs :tiles)
    )
  )

;first parameter is geo-object
(defn init-geo-object [geo comp]
  ;todo add comp size validation w vs width and if it can be placed on map
  (reduce merge-map empty-map
    (let [
      tiles (lookup-tiles geo comp)
      ]
      (for [
        i (range (:w geo))
        j (range (:h geo))
        ]
        (assoc-in empty-map
          [(+ j -1 (:y geo)) (+ i -1 (:x geo))]
          (-> tiles (get j) (get i))
          )
        )
      )
    )
  )

(defn map-geo [geo-list]
  (map
    init-geo-object
    geo-list
    (filter
      identity
      (map find-comp geo-list)
      )
    )
  )

(defn replace-nulls [rows]
  (map
    #(map (fn [tile] (or tile {:fileId :empty})) %)
    rows
    )
  )

(defn render-foreground-tiles [map-entity]
  (let [
    legend (-> map-entity :attrs :legend map-invert)
    ]
    (->> map-entity :attrs :tiles
      (map
        (fn [row]
          (->> (split row #" ")
            (map #(legend %))
            (map #(@foreground-tiles-atom %))
            )
          )
        )
      )
    )
  )

(defn render-objects-layer [entity]
  (->> entity :attrs :geo-objects
    map-geo
    (cons (render-foreground-tiles entity))
    (reduce merge-map empty-map)
    replace-nulls
    )
  )

(defn render-background-layer [map-entity]
  (let [
    legend (-> map-entity :attrs :legend map-invert)
    backgrounds (-> map-entity :attrs :backgrounds)
    ]
    (->> map-entity :attrs :tiles
      (map
        (fn [row]
          (->> (split row #" ")
            (map #(legend %))
            (map #(find-tile-or-default %
              (or (-> % backgrounds keyword) :green)))
            )
          )
        )
      )
    )
  )

(defn convert-room-map-entity [entity]{
  :roomId (-> entity :attrs :name)
  :worldId (:world entity)
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
