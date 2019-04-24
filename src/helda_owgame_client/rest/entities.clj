(ns helda-owgame-client.rest.entities
  (:require
    [clj-http.client :refer [get post]]
    )
  )

(defn read [resp]
  (:body resp)
  )

(defn find-entities
  ([world]
    (read
      (get
        "http://localhost:3000/entities/entities-by-world"
        {
          :query-params {:world world}
          :content-type :json
          :as :json-strict
          }
        )
      )
    )
  ([world models]
    (read
      (get
        "http://localhost:3000/entities/entities-by-models"
        {
          :query-params {:world world :models models}
          :content-type :json
          :as :json-strict
          }
        )
      )
    )
  ([world models tags]
    (read
      (get
        "http://localhost:3000/entities/entities-by-tags-and-models"
        {
          :query-params {:world world :models models :tags tags}
          :content-type :json
          :as :json-strict
          }
        )
      )
    )
  )

(defn perform-action [action source-id target-id action-ctx]
  (post
    "http://localhost:3000/entities/perform-action"
    {
      :form-params {
        :action action
        :source-entity-id source-id
        :target-entity-id target-id
        :action-ctx action-ctx
      }
      :content-type :json
      :as :json-strict
      }
    )
  )
