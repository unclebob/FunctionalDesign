(ns prime-factors.core)

(defn prime-factors-of [n]
    (loop [n n divisor 2 factors []]
      (if (> n 1)
        (if (zero? (rem n divisor))
          (recur (quot n divisor) divisor (conj factors divisor))
          (recur n (inc divisor) factors))
        factors)))

