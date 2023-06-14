(ns wator.fish-imp
  (:require [wator
             [config :as config]
             [cell :as cell]
             [animal :as animal]
             [fish :as fish]
             [shark :as shark]]))

(defmethod cell/tick ::fish/fish [fish loc world]
  (if (> (rand) config/fish-evolution-rate)
    [nil {loc (shark/make)}]
    (animal/tick fish loc world)))

(defmethod animal/move ::fish/fish [fish loc world]
  (animal/do-move fish loc world))

(defmethod animal/reproduce ::fish/fish [fish loc world]
  (animal/do-reproduce fish loc world))

