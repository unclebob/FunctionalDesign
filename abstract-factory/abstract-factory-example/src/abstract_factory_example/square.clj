(ns abstract-factory-example.square
  (:require [abstract-factory-example.shape :as shape]))

(defn make [top-left side]
  {::shape/type ::square
   ::top-left top-left
   ::side side})

(defmethod shape/to-string ::square [square]
    (let [[x y] (::top-left square)
          side (::side square)]
      (format "Square top-left: [%s,%s] side: %s" x y side)))
