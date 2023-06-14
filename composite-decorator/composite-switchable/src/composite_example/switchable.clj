(ns composite-example.switchable)

(defmulti turn-on :type)
(defmulti turn-off :type)