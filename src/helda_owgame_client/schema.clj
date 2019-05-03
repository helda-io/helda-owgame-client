(ns helda-owgame-client.schema
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

(s/defschema Ability {
  :id s/Str
  :name s/Str
  (s/optional-key :description) s/Str
  :level s/Int
  })

(s/defschema Level {
  :xp s/Int
  :level s/Int
  :max-hp s/Int
  :abilities [Ability]
  })

(s/defschema RpgCharacter {
  :id s/Str
  :name s/Str
  :hp s/Int
  :location Location
  :level Level
  :equipment Equipment
  :inventory Inventory
  })
