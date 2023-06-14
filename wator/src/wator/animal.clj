(ns wator.animal
  (:require [clojure.spec.alpha :as s]
            [wator
             [world :as world]
             [cell :as cell]
             [water :as water]]))

(s/def ::age int?)
(s/def ::animal (s/keys :req [::age]))

(defmulti move (fn [animal & args] (::cell/type animal)))
(defmulti reproduce (fn [animal & args] (::cell/type animal)))
(defmulti make-child ::cell/type)
(defmulti get-reproduction-age ::cell/type)

(defn make []
  {::age 0})

(defn age [animal]
  (::age animal))

(defn set-age [animal age]
  (assoc animal ::age age))

(defn increment-age [animal]
  (update animal ::age inc))

(defn tick [animal loc world]
  (let [aged-animal (increment-age animal)
        reproduction (reproduce aged-animal loc world)]
    (if reproduction
      reproduction
      (move aged-animal loc world))))

(defn do-move [animal loc world]
  (let [neighbors (world/neighbors world loc)
        moved-into (get world :moved-into #{})
        available-neighbors (remove moved-into neighbors)
        destinations (filter #(water/is? (world/get-cell world %))
                             available-neighbors)
        new-location (if (empty? destinations)
                       loc
                       (rand-nth destinations))]
    (if (= new-location loc)
      [nil {loc animal}]
      [{loc (water/make)} {new-location animal}])))

(defn do-reproduce [animal loc world]
  (if (>= (age animal) (get-reproduction-age animal))
    (let [neighbors (world/neighbors world loc)
          birth-places (filter #(water/is? (world/get-cell world %))
                               neighbors)]
      (if (empty? birth-places)
        nil
        [{loc (set-age animal 0)}
         {(rand-nth birth-places) (make-child animal)}]))
    nil))

