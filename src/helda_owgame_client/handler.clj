(ns helda-owgame-client.handler
  (:require
    [plumbing.core :refer [defnk]]
    [kekkonen.cqrs :refer :all]
    [schema.core :as s]

    [helda-owgame-client.schema :as hs]
    [helda-owgame-client.maps :as maps]
    )
  )

(defnk ^:query load-map
  "Retrieves all entities for world."
  {:responses {:default {:schema [hs/TileMap]}}}
  [
    [:components caches]
    [:data
      world :- s/Str
      room :- s/Str
      ]
    ]
  (success
    (maps/load-map world room)
    )
  )


;;
;; Application
;;

(defn create [system]
  (cqrs-api
    {:swagger {:ui "/"
               :spec "/swagger.json"
               :data {:info {:title "Kekkonen helda-owgame-client API"
                             :description "created with http://kekkonen.io"}}}
     :core {:handlers {
       :game [#'load-map]
     }
            :context system}}))
