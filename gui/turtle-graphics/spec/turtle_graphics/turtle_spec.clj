(ns turtle-graphics.turtle-spec
  (:require [speclj.core :refer :all]
            [turtle-graphics.turtle :as t]))

(declare turtle)

(defn heading [turtle heading]
  (assoc turtle :heading heading))

(defn velocity [turtle velocity]
  (assoc turtle :velocity velocity))

(defn position [turtle position]
  (assoc turtle :position position))

(defn distance [turtle distance]
  (assoc turtle :distance distance))

(defn omega [turtle omega]
  (assoc turtle :omega omega))

(defn angle [turtle angle]
  (assoc turtle :angle angle))

(defn state [turtle state]
  (assoc turtle :state state))


(describe "Turtle Update"
  (with turtle (-> (t/make) (position [1.0 1.0]) (heading 1.0)))
  (context "position update"
    (it "holds position when there's no velocity"
      (let [turtle (-> @turtle (velocity 0.0) (state :idle))
            new-turtle (t/update-turtle turtle)]
        (should= turtle new-turtle)))

    (it "steps by velocity when distance is far"
      (let [turtle (-> @turtle
                       (heading 0.0)
                       (velocity 5.0)
                       (distance 100.0)
                       (state :busy)
                       )
            {:keys [position state velocity distance]} (t/update-turtle turtle)]
        (should= [6.0 1.0] position)
        (should= 5.0 velocity)
        (should= 95.0 distance)
        (should= :busy state)))

    (it "steps back by velocity when distance is far"
      (let [turtle (-> @turtle
                       (heading 0.0)
                       (velocity -5.0)
                       (distance 100.0)
                       (state :busy)
                       )
            {:keys [position state velocity distance]} (t/update-turtle turtle)]
        (should= [-4.0 1.0] position)
        (should= -5.0 velocity)
        (should= 95.0 distance)
        (should= :busy state)))

    (it "steps by distance and goes idle when distance is near"
      (let [turtle (-> @turtle
                       (heading 0.0)
                       (velocity 5.0)
                       (distance 3.0)
                       (state :busy)
                       )
            {:keys [position state velocity distance]} (t/update-turtle turtle)]
        (should= [4.0 1.0] position)
        (should= 0.0 velocity)
        (should= 0.0 distance)
        (should= :idle state))))

  (it "steps back by distance and goes idle when distance is near"
    (let [turtle (-> @turtle
                     (heading 0.0)
                     (velocity -5.0)
                     (distance 3.0)
                     (state :busy))
          {:keys [position state velocity distance]} (t/update-turtle turtle)]
      (should= [-2.0 1.0] position)
      (should= 0.0 velocity)
      (should= 0.0 distance)
      (should= :idle state)))

  (context "angle update"
    (it "holds angle when there's no omega"
      (let [turtle (-> @turtle (omega 0) (heading 90) (angle 30) (state :idle))
            new-turtle (t/update-turtle turtle)]
        (should= turtle new-turtle)))

    (it "steps by omega when angle is far"
      (let [turtle (-> @turtle
                       (omega 5.0)
                       (angle 100.0)
                       (state :busy))
            {:keys [heading state omega angle]} (t/update-turtle turtle)]
        (should= 6.0 heading)
        (should= 5.0 omega)
        (should= 95.0 angle)
        (should= :busy state)))

    (it "steps back by omega when angle is far"
      (let [turtle (-> @turtle
                       (omega -5.0)
                       (angle 100.0)
                       (state :busy))
            {:keys [heading state omega angle]} (t/update-turtle turtle)]
        (should= 356.0 heading)
        (should= -5.0 omega)
        (should= 95.0 angle)
        (should= :busy state)))

    (it "steps by omega and goes idle when angle is near"
      (let [turtle (-> @turtle
                       (omega 5.0)
                       (angle 3.0)
                       (state :busy))
            {:keys [heading state omega angle]} (t/update-turtle turtle)]
        (should= 4.0 heading)
        (should= 0.0 omega)
        (should= 0.0 angle)
        (should= :idle state)))

    (it "steps back by omega and goes idle when angle is near"
      (let [turtle (-> @turtle
                       (omega -5.0)
                       (angle 3.0)
                       (state :busy))
            {:keys [heading state omega angle]} (t/update-turtle turtle)]
        (should= 358.0 heading)
        (should= 0.0 omega)
        (should= 0.0 angle)
        (should= :idle state))))

  (context "pen up and down"
    (it "marks the starting coordinate upon pen down"
      (let [turtle (t/pen-down @turtle)
            pen (:pen turtle)
            pen-start (:pen-start turtle)]
        (should= :down pen)
        (should= [1.0 1.0] pen-start)))

    (it "does not mark starting position if pen already down"
      (let [turtle (-> @turtle (t/pen-down) (position [2.0 2.0]) (t/pen-down))
            pen (:pen turtle)
            pen-start (:pen-start turtle)]
        (should= :down pen)
        (should= [1.0 1.0] pen-start)))

    (it "adds line when pen goes back up"
      (let [turtle (-> @turtle
                       (t/weight [3])
                       (t/pen-down)
                       (position [2.0 2.0])
                       (t/pen-up))
            pen (:pen turtle)
            pen-start (:pen-start turtle)
            lines (:lines turtle)]
        (should= :up pen)
        (should-be-nil pen-start)
        (should= [{:line-start [1.0 1.0]
                   :line-end [2.0 2.0]
                   :line-weight 3}] lines)))

    (it "does not add line when pen is already up"
      (let [turtle (-> @turtle (position [2.0 2.0]) (t/pen-up))
            pen (:pen turtle)
            pen-start (:pen-start turtle)
            lines (:lines turtle)]
        (should= :up pen)
        (should-be-nil pen-start)
        (should= [] lines))
      )

    (it "adds line upon idle after move"
      (let [turtle (->
                     @turtle
                     (heading 0)
                     (t/pen-down)
                     (t/weight [3])
                     (t/forward [1])
                     (t/update-turtle))
            pen (:pen turtle)
            state (:state turtle)
            lines (:lines turtle)
            pen-start (:pen-start turtle)
            position (:position turtle)]
        (should= :down pen)
        (should= :idle state)
        (should= position pen-start)
        (should= [{:line-start [1.0 1.0]
                   :line-end [2.0 1.0]
                   :line-weight 3}] lines)))
    )
  )
