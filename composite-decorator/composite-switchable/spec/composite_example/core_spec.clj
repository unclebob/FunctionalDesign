(ns composite-example.core-spec
  (:require [speclj.core :refer :all]
            [composite-example
             [light :as l]
             [variable-light :as v]
             [switchable :as s]
             [composite-switchable :as cs]]))

(describe "composite-switchable"
  (with-stubs)
  (it "turns all on"
    (with-redefs [l/turn-on-light (stub :turn-on-light)
                  v/set-light-intensity (stub :set-light-intensity)]
      (let [group (-> (cs/make-composite-switchable)
                      (cs/add (l/make-light))
                      (cs/add (v/make-variable-light)))]
        (s/turn-on group)
        (should-have-invoked :turn-on-light)
        (should-have-invoked :set-light-intensity {:with [100]})))))
