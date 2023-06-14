(ns stm.core-spec
  (:require [speclj.core :refer :all]
            [stm.core :refer :all]))

(describe "stm"
  (it "counts properly"
    (reset! counter 0)
    (let [ta (future (increment 20 "a"))
          tx (future (increment 20 "x"))
          done [@ta @tx] ]
      (should= 40 @counter))))
