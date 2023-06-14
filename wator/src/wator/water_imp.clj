(ns wator.water-imp
  (:require [wator
             [cell :as cell]
             [water :as water]
             [fish :as fish]
             [config :as config]]))

(defmethod cell/tick ::water/water [water loc world]
  (if (> (rand) config/water-evolution-rate)
    [nil {loc (fish/make)}]
    [nil {loc water}]))
