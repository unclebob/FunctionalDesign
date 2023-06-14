(ns iterative-fib.core-spec
  (:require [speclj.core :refer :all]
            [iterative-fib.core :refer :all]))

(describe "fib"
  (it "works"
    (should= [1] (fibs 1))
    (should= [1 1] (fibs 2))
    (should= [1 1 2] (fibs 3))
    (should= [1 1 2 3 5 8] (fibs 6))
    )

  )
