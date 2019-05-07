(ns helda-owgame-client.schema
  (:require
    [schema.core :as s]
    )
  )

(s/defschema Tile {:fileId s/Str :x s/Int :y s/Int})

(s/defschema TileMap {
  :roomId s/Str
  :worldId s/Str
  (s/optional-key :description) s/Str
  :tilesets [s/Str]
  :layers [[Tile]]
  })

(s/defschema Location {
  :roomId s/Str
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
