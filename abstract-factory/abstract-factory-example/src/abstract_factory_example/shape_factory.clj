(ns abstract-factory-example.shape-factory)

(defmulti make (fn [factory type & args] (::type factory)))
