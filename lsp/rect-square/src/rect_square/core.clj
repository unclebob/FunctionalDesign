(ns rect-square.core)

(defn make-rect [h w]
  {:h h :w w})

(defn set-h [rect h]
  (assoc rect :h h))

(defn set-w [rect w]
  (assoc rect :w w))

(defn area [rect]
  (* (:h rect) (:w rect)))

(defn perimeter [rect]
  (let [{:keys [h w]} rect]
    (* 2 (+ h w))))

(defn minimally-increase-area [rect]
  (let [{:keys [h w]} rect]
    (cond
      (>= h w) (make-rect (inc h) w)
      (> w h) (make-rect h (inc w))
      :else :tilt)))

(defn make-square [side]
  (make-rect side side))