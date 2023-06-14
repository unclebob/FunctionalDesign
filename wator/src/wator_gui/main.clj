(ns wator-gui.main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [wator
             [world :as world]
             [water :as water]
             [fish :as fish]
             [shark :as shark]
             [world-imp]
             [water-imp]
             [fish-imp]]))

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :rgb)
  (-> (world/make 80 80)
      (world/set-cell [40 40] (fish/make)))
  )

(defn update-state [world]
  (world/tick world))

(defn draw-state [world]
  (q/background 240)
  (let [cells (::world/cells world)]
    (doseq [loc (keys cells)]
      (let [[x y] loc
            cell (get cells loc)
            x (* 12 x)
            y (* 12 y)
            color (cond
                    (water/is? cell) [255 255 255]
                    (fish/is? cell) [0 0 255]
                    (shark/is? cell) [255 0 0])]
        (q/no-stroke)
        (apply q/fill color)
        (when-not (water/is? cell)
          (q/rect x y 11 11))))))

(declare wator)

(defn ^:export -main [& args]
  (q/defsketch wator
               :title "Wator"
               :size [960 960]
               :setup setup
               :update update-state
               :draw draw-state
               :features [:keep-on-top]
               :middleware [m/fun-mode])
  args)

