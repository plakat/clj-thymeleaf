(ns
  ^{:author plakat}
  clj-thymeleaf.thymeleaf
  (:import org.thymeleaf.TemplateEngine
           org.thymeleaf.templateresolver.TemplateResolver
           org.thymeleaf.context.Context
           clj_thymeleaf.ClasspathResolver
           )
  )

(def thymeleaf-engine (ref nil))

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
  "recursively convert map keys to string keys"
  [arg]
  (if (map? arg)
    (map-vals (convert-to-string-keys arg) convert-mapobjects-to-string-keys)
    (if (or (vector? arg) (seq? arg))
      (map convert-mapobjects-to-string-keys arg)
      arg
    )
  ))

(defn create-template-resolver
  "Initialize a Thymeleaf template resolver for the Ring environment"
  []
  (let [template-resolver (org.thymeleaf.templateresolver.TemplateResolver.)]
    (doto template-resolver
      (.setResourceResolver (clj_thymeleaf.ClasspathResolver.))
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
  (if (nil? @thymeleaf-engine)
    (println "thymeleaf-engine not set. Did you call thymeleaf/init-template-engine?")
    (do
      (println "transform called with viewname=" viewname " data=" data)
      (let [ctx (org.thymeleaf.context.Context.)
            ]
        ;; only transform if viewname is a string and not empty (i.e. potentially a viewname)
        (if (and (instance? String viewname) (seq viewname))
          (do
            ;; transfer data map to context obj for transformation,
            ;; converting keyword keys to string keys:
            (when (seq (keys data)) (.setVariables ctx (convert-mapobjects-to-string-keys (convert-to-string-keys data))))
            (println "engine" @thymeleaf-engine " is transforming viewname=" viewname "with ctx=" (.getVariables ctx)) (flush)
            (.process @thymeleaf-engine viewname ctx)
            )
          ;; otherwise return whatever is contained in viewname:
          viewname
          )))
    ))