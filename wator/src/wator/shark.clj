(ns wator.shark
  (:require [clojure.spec.alpha :as s]
            [wator
             [config :as config]
             [world :as world]
             [cell :as cell]
             [water :as water]
             [fish :as fish]
             [animal :as animal]]))

(s/def ::health int?)
(s/def ::shark (s/and #(= ::shark (::cell/type %))
                      ::animal/animal
                      (s/keys :req [::health])))
(defn is? [cell]
  (= ::shark (::cell/type cell)))

(defn make []
  {:post [(s/valid? ::shark %)]}
  (merge {::cell/type ::shark
          ::health config/shark-starting-health}
         (animal/make)))

(defmethod animal/make-child ::shark [fish]
  (make))

(defmethod animal/get-reproduction-age ::shark [shark]
  config/shark-reproduction-age)

(defn health [shark]
  (::health shark))

(defn set-health [shark health]
  (assoc shark ::health health))

(defn decrement-health [shark]
  (update shark ::health dec))

(defn feed [shark]
  (let [new-health (max config/shark-max-health
                        (+ (health shark) config/shark-eating-health))]
    (assoc shark ::health new-health)))

(defn eat [shark loc world]
  (let [neighbors (world/neighbors world loc)
        fishy-neighbors (filter #(fish/is? (world/get-cell world %))
                                neighbors)]
    (if (empty? fishy-neighbors)
      nil
      [{loc (water/make)}
       {(rand-nth fishy-neighbors) (feed shark)}]))
  )

(defmethod cell/tick ::shark [shark loc world]
  (if (= 1 (health shark))
    [nil {loc (water/make)}]
    (let [aged-shark (-> shark
                         (animal/increment-age)
                         (decrement-health))]
      (if-let [reproduction (animal/reproduce aged-shark loc world)]
        reproduction
        (if-let [eaten (eat aged-shark loc world)]
          eaten
          (animal/move aged-shark loc world))))))

(defmethod animal/move ::shark [shark loc world]
  (animal/do-move shark loc world))

(defmethod animal/reproduce ::shark [shark loc world]
  (if (< (health shark) config/shark-reproduction-health)
    nil
    (if-let [reproduction (animal/do-reproduce shark loc world)]
      (let [[from to] reproduction
            from-loc (-> from keys first)
            to-loc (-> to keys first)
            daughter-health (quot (health shark) 2)
            from-shark (-> from vals first (set-health daughter-health))
            to-shark (-> to vals first (set-health daughter-health))]
        [{from-loc from-shark}
         {to-loc to-shark}])
      nil)))

