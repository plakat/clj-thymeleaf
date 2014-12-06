(ns
  ^{:author plakat}
  clj-thymeleaf.thymeleaf
  (:import org.thymeleaf.TemplateEngine
           org.thymeleaf.templateresolver.TemplateResolver
           org.thymeleaf.context.Context
           gbquery.util.ClasspathResolver
           )
  )

(def thymeleaf-engine (ref {}))

(defn map-keys
  "Given a map and a function, returns the map resulting from applying the function to each key."
  [m f]
  (zipmap (map f (keys m)) (vals m)))

;; see Clojure Cookbook p.98
(defn map-vals
  "Given a map and a function, returns the map resulting from applying the function to each value."
  [m f]
  (zipmap (keys m) (map f (vals m))))

(defn convert-to-string-keys
  "convert map from keyword keys to string keys"
  [map]
  (map-keys map name)
  )

(defn convert-mapobjects-to-string-keys
  "convert each map object contained in the given map to a map with string keys"
  [map]
  (map-vals map #(if (instance? clojure.lang.PersistentArrayMap %) (map-keys % name) %))
  )

(defn create-template-resolver
  "Initialize a Thymeleaf template resolver for the Ring environment"
  []
  (let [template-resolver (org.thymeleaf.templateresolver.TemplateResolver.)]
    (doto template-resolver
      (.setResourceResolver (gbquery.util.ClasspathResolver.))
      ;(.setTemplateMode "XHTML")
      (.setPrefix "public/")
      (.setSuffix ".html")
      )))

(defn init-template-engine
  "Initialize Thymeleaf template engine and store it in @thymeleaf-engine"
  [params]
  ;; create template resolver
  (let [template-resolver (create-template-resolver)]
    ;; init template engine
    (dosync
      (ref-set thymeleaf-engine (doto (org.thymeleaf.TemplateEngine.)
                                  (.setTemplateResolver template-resolver)
                                  (.initialize)
                                  )
        ))))

(defn transform
  "Retrieve template 'viewname' and transform using provided data map. Returns the rendered view as a String"
  [viewname data]
  (println "transform called with viewname=" viewname " data=" data)
  (let [ctx (org.thymeleaf.context.Context.)
        ]
    ;; only transform if viewname is a string (i.e. potentially a viewname)
    (if (and (instance? String viewname) (seq viewname))
      (do
        ;; transfer data map to context obj for transformation,
        ;; converting keyword keys to string keys:
        (when (seq (keys data)) (.setVariables ctx (convert-mapobjects-to-string-keys (convert-to-string-keys data))))
        (println "transforming viewname=" viewname "with ctx=" (.getVariables ctx)) (flush)
        (.process @thymeleaf-engine viewname ctx)
        )
      ;; otherwise return whatever is contained in viewname:
      viewname
    )
  ))