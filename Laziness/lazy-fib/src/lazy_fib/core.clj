(ns lazy-fib.core)

(declare fib)

(defn fib-w [n]
  (cond
    (< n 1) nil
    (<= n 2) 1N
    :else (+ (fib (dec n)) (fib (- n 2)))))

(def fib (memoize fib-w))

(defn lazy-fibs []
  (map fib (rest (range)))
  )

