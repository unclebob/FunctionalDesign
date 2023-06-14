(ns turn-on-light.variable-light-adapter
  (:require [turn-on-light.switchable :as s]
            [turn-on-light.variable-light :as v-l]))

(defn make-adapter [min-intensity max-intensity]
  {:type :variable-light
   :min-intensity min-intensity
   :max-intensity max-intensity})

(defmethod s/turn-on :variable-light [variable-light]
  (v-l/turn-on-light (:max-intensity variable-light)))

(defmethod s/turn-off :variable-light [variable-light]
  (v-l/turn-on-light (:min-intensity variable-light)))