(ns abstract-factory-example.shape)

(defmulti to-string ::type)

