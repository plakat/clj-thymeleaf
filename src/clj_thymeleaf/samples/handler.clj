(ns clj-thymeleaf.samples.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [gbquery.util.thymeleaf-wrapper :as thymeleaf-wrapper]
            [gbquery.util.thymeleaf :as thymeleaf]))

(def thymeleaf-params {})

(defn init []
  (println "Inizializing Thymeleaf Engine with params=" thymeleaf-params)
  (thymeleaf/init-template-engine thymeleaf-params)
  )

(defn destroy []
  )

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes app-routes)
    (handler/site)
    (thymeleaf-wrapper/wrap)))


