(ns gbquery.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [gbquery.routes.home :refer [home-routes]]
            [gbquery.routes.search :refer [perform-search suggest-streetnames]]
            [gbquery.routes.blacklist :as blacklist]
            [gbquery.models.db :as db]
            [gbquery.util.thymeleaf-wrapper :as thymeleaf-wrapper]
            [gbquery.util.thymeleaf :as thymeleaf]))

(def database-filename "./gbdata.h2")
(def thymeleaf-params {})

(defn init []
  (println "gbquery is starting")
  (db/init-datasource database-filename)
  (println "Successfully loaded database from" database-filename)
  (db/update-db)
  (println "Inizializing Thymeleaf Engine with params=" thymeleaf-params)
  (thymeleaf/init-template-engine thymeleaf-params)
  )

(defn destroy []
  (println "gbquery is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (GET "/search" [:as {params :params}] (perform-search params))
  (GET "/suggest" [:as {params :params}] (suggest-streetnames params))

  (GET "/blacklist" [:as {params :params}] (blacklist/show-blacklist params))
  (POST "/blacklist/list" [:as {params :params}] (blacklist/list-entries params))
  (POST "/blacklist/create" [:as {params :params}] (blacklist/create-entry params))
  (POST "/blacklist/update" [:as {params :params}] (blacklist/update-entry params))
  (POST "/blacklist/delete" [:as {params :params}] (blacklist/delete-entry params))

  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
    (handler/site)
    ;(wrap-base-url)
    (thymeleaf-wrapper/wrap)))


