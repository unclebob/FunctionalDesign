(ns gossiping-bus-drivers-clojure.core-spec
  (:require [speclj.core :refer :all]
            [gossiping-bus-drivers-clojure.core :refer :all]))

(describe "gossiping bus drivers"
  (it "drives one bus at one stop"
    (let [driver (make-driver "d1" [:s1] #{:r1})
          world [driver]
          new-world (drive world)]
      (should= 1 (count new-world))
      (should= :s1 (-> new-world first :route first))))

  (it "drives one bus at two stops"
    (let [driver (make-driver "d1" [:s1 :s2] #{:r1})
          world [driver]
          new-world (drive world)]
      (should= 1 (count new-world))
      (should= :s2 (-> new-world first :route first))))

  (it "drives two busses at some stops"
    (let [d1 (make-driver "d1" [:s1 :s2] #{:r1})
          d2 (make-driver "d2" [:s1 :s3 :s2] #{:r2})
          world [d1 d2]
          new-1 (drive world)
          new-2 (drive new-1)]
      (should= 2 (count new-1))
      (should= :s2 (-> new-1 first :route first))
      (should= :s3 (-> new-1 second :route first))
      (should= 2 (count new-2))
      (should= :s1 (-> new-2 first :route first))
      (should= :s2 (-> new-2 second :route first))))

  (it "gets stops"
    (let [drivers #{{:name "d1" :route [:s1]}
                    {:name "d2" :route [:s1]}
                    {:name "d3" :route [:s2]}}]
      (should= {:s1 [{:name "d1" :route [:s1]}
                     {:name "d2" :route [:s1]}]
                :s2 [{:name "d3", :route [:s2]}]}
               (get-stops drivers)))
    )

  (it "merges rumors"
    (should= [{:name "d1" :rumors #{:r2 :r1}}
              {:name "d2" :rumors #{:r2 :r1}}]
             (merge-rumors [{:name "d1" :rumors #{:r1}}
                            {:name "d2" :rumors #{:r2}}])))


  (it "shares gossip when drivers are at same stop"
    (let [d1 (make-driver "d1" [:s1 :s2] #{:r1})
          d2 (make-driver "d2" [:s1 :s2] #{:r2})
          world [d1 d2]
          new-world (drive world)]
      (should= 2 (count new-world))
      (should= #{:r1 :r2} (-> new-world first :rumors))
      (should= #{:r1 :r2} (-> new-world second :rumors))))

  (it "passes acceptance test 1"
    (let [world [(make-driver "d1" [3 1 2 3] #{1})
                 (make-driver "d2" [3 2 3 1] #{2})
                 (make-driver "d3" [4 2 3 4 5] #{3})]]
      (should= 6 (drive-till-all-rumors-spread world))))

  (it "passes acceptance test 2"
    (let [world [(make-driver "d1" [2 1 2] #{1})
                 (make-driver "d2" [5 2 8] #{2})]]
          (should= :never (drive-till-all-rumors-spread world))))
  )

