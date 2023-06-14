(ns turn-on-light.turn-on-spec
  (:require [speclj.core :refer :all]
            [turn-on-light.engage-switch :refer :all]
            [turn-on-light.variable-light :as v-l]
            [turn-on-light.variable-light-adapter :as v-l-adapter]))

(describe "Adapter"
  (with-stubs)
  (it "turns light on and off"
    (with-redefs [v-l/turn-on-light (stub :turn-on-light)]
      (engage-switch (v-l-adapter/make-adapter 5 90))
      (should-have-invoked :turn-on-light {:times 1 :with [90]})
      (should-have-invoked :turn-on-light {:times 1 :with [5]}))))


