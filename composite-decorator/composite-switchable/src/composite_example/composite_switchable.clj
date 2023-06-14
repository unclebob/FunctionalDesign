(ns composite-example.composite-switchable
  (:require [composite-example.switchable :as s]))

(defn make-composite-switchable []
  {:type :composite-switchable
   :switchables []})

(defn add [composite-switchable switchable]
  (update composite-switchable :switchables conj switchable))

(defmethod s/turn-on :composite-switchable [c-switchable]
  (doseq [s-able (:switchables c-switchable)]
    (s/turn-on s-able)))

(defmethod s/turn-off :composite-switchable [c-switchable]
  (doseq [s-able (:switchables c-switchable)]
    (s/turn-off s-able)))
