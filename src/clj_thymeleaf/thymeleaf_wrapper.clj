(ns
  ^{:author plakat}
  clj-thymeleaf.thymeleaf-wrapper
  (:require [clj-thymeleaf.thymeleaf :as thymeleaf])
  )

(defn wrap
  "Apply Thymeleaf templating to the result. Returns a function that expects key :body to hold the view name (without extension) and key :data to hold a map of data"
  [handler]
  (fn [request]
    (let [response (handler request)
          viewname (:viewname response)
          data (:data response)
          ]
      ;; todo: add :headers {"Content-Type" "text/html;charset=utf-8"}
      (if viewname
        ;; if viewname is set transform using thymeleaf,
        ;; returning the result of the transformation as :body
        (assoc response :body (thymeleaf/transform viewname data))
        ;; otherwise just pass along existing body
        response
        )))
  )