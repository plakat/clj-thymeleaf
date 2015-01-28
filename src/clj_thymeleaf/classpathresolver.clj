(ns
  ^{:author muecke}
  clj-thymeleaf.classpathresolver
  (:import java.io.InputStream
           org.thymeleaf.resourceresolver.IResourceResolver
           org.thymeleaf.TemplateProcessingParameters
           )
  (:require [clojure.java.io :as io])
  )

(gen-class
  :name clj_thymeleaf.ClasspathResolver
  :implements [org.thymeleaf.resourceresolver.IResourceResolver]
  )

(defn- -getName [this]
  "CLASSPATH")

(defn- -getResourceAsStream [this params name]
  (println "classpathresolver looking for template " name)
  (when-let [resource-data (io/resource name)]
    (io/input-stream resource-data :encoding "UTF-8")
    )
  )
