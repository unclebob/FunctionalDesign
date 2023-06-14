(ns video-store.statement-policy-spec
  (:require [speclj.core :refer :all]
            [video-store.constructors :refer :all]
            [video-store.statement-policy :refer :all :as policy]
            [video-store.normal-statement-policy :refer :all]
            [video-store.buy-two-get-one-free-policy :refer :all]
            [clojure.spec.alpha :as s]))

(declare customer normal-policy formatter)
(declare new-release-1 new-release-2 childrens)
(declare regular-1 regular-2 regular-3)

(describe "Rental Statement Calculation"
  (with customer (make-customer "CUSTOMER"))
  (with normal-policy (make-normal-policy))
  (with new-release-1 (make-movie "new release 1" :new-release))
  (with new-release-2 (make-movie "new release 2" :new-release))
  (with childrens (make-movie "childrens" :childrens))
  (with regular-1 (make-movie "regular 1" :regular))
  (with regular-2 (make-movie "regular 2" :regular))
  (with regular-3 (make-movie "regular 3" :regular))
  (context "normal policy"
    (it "makes statement for a single new release"
      (let [statement-data (make-statement-data
                             @normal-policy
                             (make-rental-order
                               @customer
                               [(make-rental @new-release-1 3)]))]
        (should-be-nil (s/explain-data ::policy/statement-data statement-data))
        (should= {:customer-name "CUSTOMER"
                  :movies [{:title "new release 1"
                            :price 9.0}]
                  :owed 9.0
                  :points 2}
                 statement-data)))

    (it "makes statement for two new releases"
      (should= {:customer-name "CUSTOMER",
                :movies [{:title "new release 1", :price 9.0}
                         {:title "new release 2", :price 9.0}],
                :owed 18.0,
                :points 4}
               (make-statement-data
                 @normal-policy
                 (make-rental-order
                   @customer
                   [(make-rental @new-release-1 3)
                    (make-rental @new-release-2 3)]))))

    (it "makes statement for one childrens movie"
      (should= {:customer-name "CUSTOMER",
                :movies [{:title "childrens", :price 1.5}],
                :owed 1.5,
                :points 1}
               (make-statement-data
                 @normal-policy
                 (make-rental-order
                   @customer
                   [(make-rental @childrens 3)]))))

    (it "makes statement for several regular movies"
      (should= {:customer-name "CUSTOMER",
                :movies [{:title "regular 1", :price 2.0}
                         {:title "regular 2", :price 2.0}
                         {:title "regular 3", :price 3.5}],
                :owed 7.5,
                :points 3}
               (make-statement-data
                 @normal-policy
                 (make-rental-order
                   @customer
                   [(make-rental @regular-1 1)
                    (make-rental @regular-2 2)
                    (make-rental @regular-3 3)])))))

  (context "Buy two get one free policy"
    (it "makes statement for several regular movies"
      (should= {:customer-name "CUSTOMER",
                :movies [{:title "regular 1", :price 2.0}
                         {:title "regular 2", :price 2.0}
                         {:title "new release 1", :price 3.0}],
                :owed 5.0,
                :points 3}
               (make-statement-data
                 (make-buy-two-get-one-free-policy)
                 (make-rental-order
                   @customer
                   [(make-rental @regular-1 1)
                    (make-rental @regular-2 1)
                    (make-rental @new-release-1 1)]))))))