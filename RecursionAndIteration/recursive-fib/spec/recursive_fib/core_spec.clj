(ns recursive-fib.core-spec
  (:require [speclj.core :refer :all]
            [recursive-fib.core :refer :all]))

(describe "recursive-fib"
  (it "works"
    (should= nil (fib 0))
    (should= 1 (fib 1))
    (should= 1 (fib 2))
    (should= 2 (fib 3))
    (should= 3 (fib 4))
    (should= 5 (fib 5))
    (should= [1 1 2 3 5 8 13] (fibs 7))
    (should= nil (fib 0))
    (should= 1 (ifib 1))
    (should= 1 (ifib 2))
    (should= 2 (ifib 3))
    (should= 3 (ifib 4))
    (should= 5 (ifib 5))
    ))
