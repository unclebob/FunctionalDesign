(ns switch-light.core)

(defn turn-on-light []
  ;turn on the bloody light!
  )

(defn turn-off-light []
  ;Criminy! just turn it off!
  )

(defmulti turn-on :type)
(defmulti turn-off :type)

(defmethod turn-on :light [switchable]
  (turn-on-light))

(defmethod turn-off :light [switchable]
  (turn-off-light))

(defn engage-switch [switchable]
  ;Some other stuff...
  (turn-on switchable)
  ;Some more other stuff...
  (turn-off switchable))


