(ns abstract-factory-example.shape-factory-implementation
  (:require [abstract-factory-example
             [shape-factory :as factory]
             [square :as square]
             [circle :as circle]]))

(defn make []
  {::factory/type ::implementation})

(defmethod factory/make ::implementation
  [factory type & args]
  (condp = type
    :square (apply square/make args)
    :circle (apply circle/make args))
  )

