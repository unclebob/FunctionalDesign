(ns word-wrap.core)

(defn wrap [s w]
  (if (<= (count s) w)
    s
    (let [cut-at (.lastIndexOf s " " w)
          cut-at (if (neg? cut-at) w cut-at)]
      (str (.trim (subs s 0 cut-at)) "\n"  (wrap (.trim (subs s cut-at)) w)))))