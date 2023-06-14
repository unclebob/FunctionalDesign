(ns video-store.quick-check
  (:require [speclj.core :refer :all]
            [video-store.constructors :refer :all :as constructors]
            [video-store.normal-statement-policy :refer :all]
            [video-store.buy-two-get-one-free-policy :refer :all]
            [video-store.statement-policy :refer :all :as policy]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.spec.alpha :as s]))

(def gen-customer-name (gen/such-that not-empty gen/string-alphanumeric))

(def gen-customer
  (gen/fmap (fn [name] {:name name}) gen-customer-name))

(def gen-days (gen/elements (range 1 100)))

(def gen-movie-type (gen/elements [:regular :childrens :new-release]))

(def gen-movie
  (gen/fmap (fn [[title type]] {:title title :type type})
            (gen/tuple gen/string-alphanumeric gen-movie-type)))

(def gen-rental
  (gen/fmap (fn [[movie days]] {:movie movie :days days})
            (gen/tuple gen-movie gen-days)))

(def gen-rentals (gen/such-that not-empty (gen/vector gen-rental)))

(def gen-rental-order
  (gen/fmap (fn [[customer rentals]] {:customer customer :rentals rentals})
            (gen/tuple gen-customer gen-rentals)))

(declare rental-order normal b2g1f)

(describe "Quick check statement policy"
  (with normal (make-normal-policy))
  (with b2g1f (make-buy-two-get-one-free-policy))

  (it "generates valid rental orders"
    (should-be
      :result
      (tc/quick-check
        100
        (prop/for-all
          [rental-order gen-rental-order]
          (nil?
            (s/explain-data
              ::constructors/rental-order
              rental-order))))))

  (it "produces valid statement data"
    (should-be
      :result
      (tc/quick-check
        100
        (prop/for-all
          [rental-order gen-rental-order]
          (nil?
            (s/explain-data
              ::policy/statement-data
              (make-statement-data @normal rental-order)))))))

  (it "statement data totals are consistent under normal policy"
    (should-be
      :result
      (tc/quick-check
        100
        (prop/for-all
          [rental-order gen-rental-order]
          (let [statement-data (make-statement-data @normal rental-order)
                prices (map :price (:movies statement-data))
                owed (:owed statement-data)]
            (= owed (reduce + prices)))))))

  (it "statement data totals are consistent under buy-two-get-one-free policy"
    (should-be
      :result
      (tc/quick-check
        100
        (prop/for-all
          [rental-order gen-rental-order]
          (let [statement-data (make-statement-data @b2g1f rental-order)
                prices (map :price (:movies statement-data))
                owed (:owed statement-data)]
            (if (> (count prices) 2)
              (= owed (reduce + (drop 1 (sort prices))))
              (= owed (reduce + prices)))))))))



