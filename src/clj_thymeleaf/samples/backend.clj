(ns
  ^{:author muecke}
  clj_thymeleaf.samples.backend)

(defn perform-action [params]
  (let [resultmap {:key1 "data" :key2 "more data"}]
    {
      :status 200
      :headers {"Content-Type" "text/html;charset=utf-8"}
      :viewname "templatename"
      ;; the value of key :data is provided as a data map to the template:
      :data (assoc params
              :result (map thymeleaf/convert-to-string-keys (doall resultmap)))
      }
    )
  )
