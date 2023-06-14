(ns visitor-example.json-shape-visitor
  (:require [visitor-example
             [shape :as shape]]))

(defmulti to-json ::shape/type)

