(ns abstract-factory-example.core-spec
  (:require [speclj.core :refer :all]
            [abstract-factory-example
             [shape :as shape]
             [shape-factory :as factory]
             [main :as main]]))

(describe "Shape Factory"
  (before-all (main/init))
  (it "creates a square"
    (let [square (factory/make
                   @main/shape-factory
                   :square
                   [100 100] 10)]
      (should= "Square top-left: [100,100] side: 10"
               (shape/to-string square))))

  (it "creates a circle"
      (let [circle (factory/make
                     @main/shape-factory
                     :circle
                     [100 100] 10)]
        (should= "Circle center: [100,100] radius: 10"
                 (shape/to-string circle)))))

