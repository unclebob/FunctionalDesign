(ns factors.core-spec
  (:require [speclj.core :refer :all]
            [factors.core :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

(defn power2 [n]
  (apply * (repeat n 2N)))

(describe "factor primes"
  (it "factors 1 -> []"
    (should= [] (factors-of 1)))
  (it "factors 2 -> [2]"
    (should= [2] (factors-of 2)))
  (it "factors 3 -> [3]"
    (should= [3] (factors-of 3)))
  (it "factors 4 -> [2 2]"
    (should= [2 2] (factors-of 4)))
  (it "factors 5 -> [5]"
    (should= [5] (factors-of 5)))
  (it "factors 6 -> [2 3]"
    (should= [2 3] (factors-of 6)))
  (it "factors 7 -> [7]"
    (should= [7] (factors-of 7)))
  (it "factors 8 -> [2 2 2]"
    (should= [2 2 2] (factors-of 8)))
  (it "factors 9 -> [3 3]"
    (should= [3 3] (factors-of 9)))
  (it "factors lots"
    (should= [2 2 3 3 5 7 11 11 13]
             (factors-of (* 2 2 3 3 5 7 11 11 13))))
  (it "factors Euler 3"
    (should= [71 839 1471 6857] (factors-of 600851475143)))

  (it "factors mersenne 2^31-1"
    (should= [2147483647] (factors-of (dec (power2 31)))))
  )

(def gen-inputs (gen/large-integer* {:min 1 :max 1E9}))

(declare n)

(describe "properties"
  (it "multiplies out properly"
    (should-be
      :result
      (tc/quick-check
        1000
        (prop/for-all
          [n gen-inputs]
          (let [factors (factors-of n)]
            (= n (reduce * factors))))))))

(defn is-prime? [n]
  (if (= 2 n)
    true
    (loop [candidates (range 2 (inc (Math/sqrt n)))]
      (if (empty? candidates)
        true
        (if (zero? (rem n (first candidates)))
          false
          (recur (rest candidates)))))))

(describe "is-prime"
  (it "tests for primes"
    (should (every? is-prime? [2 3 5 7 11 13 17 19 151])))
  (it "tests for composites"
    (should-be-nil (some is-prime? [4 6 8 9 10 12 14 15 16 18 20 21]))))

(describe "factors"
  (it "they are all prime"
    (should-be
      :result
      (tc/quick-check
      1000
      (prop/for-all
        [n gen-inputs]
        (let [factors (factors-of n)]
          (every? is-prime? factors)))))))