(ns parse-order.core-spec
  (:require [speclj.core :refer :all]
            [parse-order.core :refer :all]
            [clojure.spec.alpha :as s]))

(describe "Order Entry System"
  (context "Parsing Customers"
    (it "parses a valid customer"
      (let [customer (parse-customer
                       ["Customer-id: 1234567"
                        "Name: customer name"
                        "Address: customer address"
                        "Credit Limit: 50000"])]
        (should=
          {:id "1234567"
           :name "customer name"
           :address "customer address"
           :credit-limit 50000}
          customer)
        (should (s/valid? :parse-order.core/customer customer))))

    (it "parses invalid customer"
      (should= :invalid
               (parse-customer
                 ["Customer-id: X"
                  "Name: customer name"
                  "Address: customer address"
                  "Credit Limit: 50000"]))
      (should= :invalid
               (parse-customer
                 ["Customer-id: 1234567"
                  "Name: "
                  "Address: customer address"
                  "Credit Limit: 50000"]))
      (should= :invalid
               (parse-customer
                 ["Customer-id: 1234567"
                  "Name: customer name"
                  "Address: "
                  "Credit Limit: 50000"]))
      (should= :invalid
               (parse-customer
                 ["Customer-id: 1234567"
                  "Name: customer name"
                  "Address: customer address"
                  "Credit Limit: invalid"])))
    (it "makes sure credit limit is <= 50000"
      (should= :invalid
               (parse-customer
                 ["Customer-id: 1234567"
                  "Name: customer name"
                  "Address: customer address"
                  "Credit Limit: 50001"])))
    ))
