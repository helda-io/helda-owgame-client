(ns helda-owgame-client.handler
  (:require
    [kekkonen.cqrs :refer :all]
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
     :core {:handlers {}
            :context system}}))
