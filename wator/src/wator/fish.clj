(ns wator.fish
  (:require [clojure.spec.alpha :as s]
            [wator
             [cell :as cell]
             [animal :as animal]]
            [wator.config :as config]))

(s/def ::fish (s/and #(= ::fish (::cell/type %))
                     ::animal/animal))
(defn is? [cell]
  (= ::fish (::cell/type cell)))

(defn make []
  {:post [(s/valid? ::fish %)]}
  (merge {::cell/type ::fish}
         (animal/make)))

(defmethod animal/make-child ::fish [fish]
  (make))

(defmethod animal/get-reproduction-age ::fish [fish]
  config/fish-reproduction-age)


