(ns rect-square.core-spec
  (:require [speclj.core :refer :all]
            [rect-square.core :refer :all]))

(describe "Rectangle"
  (it "calculates proper area after change in size"
    (should= 25 (area (make-rect 5 5)))
    (should= 36 (area (make-square 6)))
    (should= 18 (perimeter (make-rect 4 5)))
    (should= 20 (perimeter (make-square 5)))
    (should= 12 (-> (make-rect 1 1) (set-h 3) (set-w 4) area))
    (should= 12 (-> (make-square 1) (set-h 3) (set-w 4) area))
    )

  (it "mimimally increases area"
    (should= 15 (-> (make-rect 3 4) minimally-increase-area area))
    (should= 24 (-> (make-rect 5 4) minimally-increase-area area))
    (should= 20 (-> (make-rect 4 4) minimally-increase-area area))
    (should= 30 (-> (make-square 5) minimally-increase-area area))))
