(ns helda.schema
  (:require
    [schema.core :as s]
    )
  )

(s/defschema TileMapLayer {
  :id s/Int
  :name s/Str
  :width s/Int
  :height s/Int
  :data [s/Int]
  })

(s/defschema TileMap {
  :room-id s/Str
  :world-id s/Str
  (s/optional-key :description) s/Str
  :tilesets [s/Str]
  :layers [TileMapLayer]
  })

(s/defschema Location {
  :room-id s/Str
  :x s/Int
  :y s/Int
  })

(s/defschema Weapon {
  :name s/Str
  :attack s/Str
  :range s/Int
  })

(s/defschema Equipment {
  :weapons [Weapon]
  })

(s/defschema Item {
  :id s/Str
  :name s/Str
  (s/optional-key :description) s/Str
  :qty s/Int
  })

(s/defschema Inventory {
  :gold s/Int
  :items [Item]
  })

(s/defschema Character {
  :id s/Str
  :name s/Str
  :hp s/Int
  :max-hp s/Int
  :location Location
  :equipment Equipment
  :inventory Inventory
})
