(ns wator.cell)

(defmulti tick (fn [cell & args] (::type cell)))

