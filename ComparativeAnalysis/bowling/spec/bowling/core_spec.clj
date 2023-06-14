(ns bowling.core-spec
  (:require [speclj.core :refer :all]
            [bowling.core :refer :all]))

(describe "The Bowling Game"
  (it "scores a gutter game"
    (should= 0 (score (repeat 20 0))))

  (it "scores all ones"
    (should= 20 (score (repeat 20 1))))

  (it "scores one spare"
    (should= 24 (score (concat [5 5 7] (repeat 17 0)))))

  (it "one strike"
    (should= 20 (score (concat [10 2 3] (repeat 16 0)))))

  (it "perfect game"
    (should= 300 (score (repeat 12 10))))
  )
