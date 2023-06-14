(ns wator.core-spec
  (:require [speclj.core :refer :all]
            [wator
             [cell :as cell]
             [water :as water]
             [water-imp]
             [animal :as animal]
             [fish :as fish]
             [fish-imp]
             [shark :as shark]
             [world :as world]
             [world-imp]]
            [wator.config :as config]))

(describe "Wator"
  (with-stubs)
  (context "Water"
    (it "usually remains water"
      (with-redefs [rand (stub :rand {:return 0.0})]
        (let [water (water/make)
              world (world/make 1 1)
              [from to] (cell/tick water [0 0] world)]
          (should-be-nil from)
          (should (water/is? (get to [0 0])))
          )))

    (it "occasionally evolves into a fish"
      (with-redefs [rand (stub :rand {:return 1.0})]
        (let [water (water/make)
              world (world/make 1 1)
              [from to] (cell/tick water [0 0] world)]
          (should-be-nil from)
          (should (fish/is? (get to [0 0])))))))

  (context "world"
    (it "creates a world full of water cells"
      (let [world (world/make 2 2)
            cells (::world/cells world)
            positions (set (keys cells))]
        (should= #{[0 0] [0 1]
                   [1 0] [1 1]} positions)
        (should (every? #(= ::water/water (::cell/type %)) (vals cells)))))

    (it "makes neighbors"
      (let [world (world/make 5 5)]
        (should= [[0 0] [0 1] [0 2]
                  [1 0] [1 2]
                  [2 0] [2 1] [2 2]]
                 (world/neighbors world [1 1]))
        (should= [[4 4] [4 0] [4 1]
                  [0 4] [0 1]
                  [1 4] [1 0] [1 1]]
                 (world/neighbors world [0 0]))
        (should= [[3 3] [3 4] [3 0]
                  [4 3] [4 0]
                  [0 3] [0 4] [0 0]]
                 (world/neighbors world [4 4]))))

    (it "moves a fish around each tick"
      (doseq [scenario [{:dimension [2 1] :starting [0 0] :ending [1 0]}
                        {:dimension [2 1] :starting [1 0] :ending [0 0]}
                        {:dimension [1 2] :starting [0 0] :ending [0 1]}
                        {:dimension [1 2] :starting [0 1] :ending [0 0]}]]
        (let [fish (fish/make)
              {:keys [dimension starting ending]} scenario
              [h w] dimension
              small-world (-> (world/make h w)
                              (world/set-cell starting fish)
                              (world/tick))
              vacated-cell (world/get-cell small-world starting)
              occupied-cell (world/get-cell small-world ending)]
          (should (water/is? vacated-cell))
          (should (fish/is? occupied-cell))
          (should= 1 (animal/age occupied-cell)))))

    (it "fills the world with reproducing fish"
      (loop [world (-> (world/make 10 10)
                       (world/set-cell [5 5] (fish/make)))
             n 100]
        (if (zero? n)
          (let [cells (-> world ::world/cells vals)
                fishies (filter fish/is? cells)
                fish-count (count fishies)]
            (should (< 50 fish-count)))
          (recur (world/tick world) (dec n)))))

    (it "move two fish who compete for the same spot"
      (let [fish (fish/make)
            competative-world (-> (world/make 3 1)
                                  (world/set-cell [0 0] fish)
                                  (world/set-cell [2 0] fish)
                                  (world/tick))
            start-00 (world/get-cell competative-world [0 0])
            start-20 (world/get-cell competative-world [2 0])
            end-10 (world/get-cell competative-world [1 0])]
        (should (fish/is? end-10))
        (should (or (fish/is? start-00)
                    (fish/is? start-20)))
        (should (or (water/is? start-00)
                    (water/is? start-20)))))
    )

  (context "animal"
    (it "moves"
      (doseq [scenario [{:constructor fish/make :tester fish/is?}
                        {:constructor shark/make :tester shark/is?}]]
        (let [animal ((:constructor scenario))
              world (-> (world/make 3 3)
                        (world/set-cell [1 1] animal))
              [from to] (animal/move animal [1 1] world)
              loc (first (keys to))]
          (should (water/is? (get from [1 1])))
          (should ((:tester scenario) (get to loc)))
          (should (#{[0 0] [0 1] [0 2]
                     [1 0] [1 2]
                     [2 0] [2 1] [2 2]}
                   loc)))))

    (it "doesn't move if there are no spaces"
      (doseq [scenario [{:constructor fish/make :tester fish/is?}
                        {:constructor shark/make :tester shark/is?}]]
        (let [animal ((:constructor scenario))
              world (-> (world/make 1 1)
                        (world/set-cell [0 0] animal))
              [from to] (animal/move animal [0 0] world)]
          (should ((:tester scenario) (get to [0 0])))
          (should (nil? from)))))

    (it "reproduces"
      (doseq [scenario [{:constructor fish/make :tester fish/is?}
                        {:constructor #(-> (shark/make)
                                           (shark/set-health (inc config/shark-reproduction-health)))
                         :tester shark/is?}]]
        (let [animal ((:constructor scenario))
              reproduction-age (animal/get-reproduction-age animal)
              animal (animal/set-age animal reproduction-age)
              world (-> (world/make 3 3)
                        (world/set-cell [1 1] animal))
              [from to] (animal/reproduce animal [1 1] world)
              from-loc (-> from keys first)
              from-cell (-> from vals first)
              to-loc (-> to keys first)
              to-cell (-> to vals first)]
          (should= from-loc [1 1])
          (should ((:tester scenario) from-cell))
          (should= 0 (animal/age from-cell))
          (should (#{[0 0] [0 1] [0 2]
                     [1 0] [1 2]
                     [2 0] [2 1] [2 2]}
                   to-loc))
          (should ((:tester scenario) to-cell))
          (should= 0 (animal/age to-cell)))))

    (it "doesn't reproduce if there is no room"
      (doseq [scenario [{:constructor fish/make :tester fish/is?}
                        {:constructor shark/make :tester shark/is?}]]
        (let [animal ((:constructor scenario))
              reproduction-age (animal/get-reproduction-age animal)
              animal (animal/set-age animal reproduction-age)
              world (-> (world/make 1 1)
                        (world/set-cell [0 0] animal))
              failed (animal/reproduce animal [0 0] world)]
          (should-be-nil failed))))

    (it "doesn't reproduce if too young"
      (doseq [scenario [{:constructor fish/make :tester fish/is?}
                        {:constructor shark/make :tester shark/is?}]]
        (let [animal ((:constructor scenario))
              reproduction-age (animal/get-reproduction-age animal)
              animal (animal/set-age animal (dec reproduction-age))
              world (-> (world/make 3 3)
                        (world/set-cell [1 1] animal))
              failed (animal/reproduce animal [1 1] world)]
          (should-be-nil failed)))))

  (context "shark"
    (it "starts with some health"
      (let [shark (shark/make)]
        (should= config/shark-starting-health
                 (shark/health shark))))

    (it "loses health with time"
      (let [small-world (-> (world/make 1 1)
                            (world/set-cell [0 0] (shark/make)))
            aged-world (world/tick small-world)
            aged-shark (world/get-cell aged-world [0 0])]
        (should= (dec config/shark-starting-health)
                 (shark/health aged-shark))))

    (it "dies when health goes to zero"
      (let [sick-shark (-> (shark/make)
                           (shark/set-health 1))
            small-world (-> (world/make 1 1)
                            (world/set-cell [0 0] sick-shark))
            aged-world (world/tick small-world)
            dead-shark (world/get-cell aged-world [0 0])]
        (should (water/is? dead-shark))))

    (it "eats when a fish is adjacent"
      (let [world (-> (world/make 2 1)
                      (world/set-cell [0 0] (fish/make))
                      (world/set-cell [1 0] (shark/make)))
            shark-ate-world (world/tick world)
            full-shark (world/get-cell shark-ate-world [0 0])
            where-shark-was (world/get-cell shark-ate-world [1 0])
            expected-health (max config/shark-max-health
                                 (+ config/shark-starting-health
                                    config/shark-eating-health
                                    -1))]
        (should (shark/is? full-shark))
        (should (water/is? where-shark-was))
        (should= expected-health (shark/health full-shark))))

    (it "shares health with both daughters after reproduction"
      (let [initial-health (inc config/shark-reproduction-health)
            pregnant-shark (-> (shark/make)
                               (animal/set-age (inc config/shark-reproduction-age))
                               (shark/set-health initial-health))
            world (-> (world/make 2 1)
                      (world/set-cell [0 0] pregnant-shark))
            new-world (world/tick world)
            daughter1 (world/get-cell new-world [0 0])
            daughter2 (world/get-cell new-world [1 0])
            expected-health (quot (dec initial-health) 2)]
        (should (shark/is? daughter1))
        (should (shark/is? daughter2))
        (should= expected-health (shark/health daughter1))
        (should= expected-health (shark/health daughter2))))

    (it "doesn't reproduce if not healthy enough"
      (let [shark (-> (shark/make)
                      (shark/set-health (dec config/shark-reproduction-health))
                      (animal/set-age config/shark-reproduction-age))
            world (-> (world/make 3 3)
                      (world/set-cell [1 1] shark))
            failed (animal/reproduce shark [1 1] world)]
        (should-be-nil failed))))

  (context "fish"
    (it "usually remains fish"
      (with-redefs [rand (stub :rand {:return 0.0})]
        (let [fish (fish/make)
              world (world/make 1 1)
              [from to] (cell/tick fish [0 0] world)]
          (should-be-nil from)
          (should (fish/is? (get to [0 0])))
          )))

    (it "occasionally evolves into a shark"
      (with-redefs [rand (stub :rand {:return 1.0})]
        (let [fish (fish/make)
              world (world/make 1 1)
              [from to] (cell/tick fish [0 0] world)]
          (should-be-nil from)
          (should (shark/is? (get to [0 0]))))))))




