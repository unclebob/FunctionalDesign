(ns lazy-fib.core-spec
  (:require [speclj.core :refer :all]
            [lazy-fib.core :refer :all]))

(describe "lazy-fib"
  (it "works"
    (should= [1 1 2 3 5 8] (take 6 (lazy-fibs)))))
