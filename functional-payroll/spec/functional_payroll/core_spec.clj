(ns functional-payroll.core-spec
  (:require [speclj.core :refer :all]
            [functional-payroll.payroll :refer :all]
            [functional-payroll.payroll-implementation]
            [functional-payroll.payroll-interface :as i]
            [clojure.spec.alpha :as s])
  (:import (java.text SimpleDateFormat)))

(defn parse-date [date-string]
  (let [sdf (SimpleDateFormat. "MMM dd yyyy")]
    (.parse sdf date-string)))

(describe "payroll"
  (it "pays no one if no one is ready"
    (let [employees []
          db {:employees employees}
          today (parse-date "Nov 14 2022")]
      (should (s/valid? ::i/db db))
      (let [paycheck-directives (payroll today db)]
        (should (s/valid? ::i/paycheck-directives paycheck-directives))
        (should= [] paycheck-directives))))

  (it "pays one salaried employee at end of month by mail"
    (let [employees [{:id "emp1"
                      :schedule :monthly
                      :pay-class [:salaried 5000]
                      :disposition [:mail "name" "home"]}]
          db {:employees employees}
          today (parse-date "Nov 30 2021")]
      (should (s/valid? ::i/db db))
      (let [paycheck-directives (payroll today db)]
        (should (s/valid? ::i/paycheck-directives paycheck-directives))
        (should= [{:type :mail
                   :id "emp1"
                   :name "name"
                   :address "home"
                   :amount 5000}]
                 paycheck-directives))))

  (it "pays one hourly employee on Friday by Direct Deposit"
    (let [employees [{:id "empid"
                      :schedule :weekly
                      :pay-class [:hourly 15]
                      :disposition [:deposit "routing" "account"]}]
          time-cards {"empid" [["Nov 12 2022" 80/10]]}
          db {:employees employees :time-cards time-cards}
          friday (parse-date "Nov 18 2022")]
      (should (s/valid? ::i/db db))
      (let [paycheck-directives (payroll friday db)]
        (should (s/valid? ::i/paycheck-directives paycheck-directives))
        (should= [{:type :deposit
                   :id "empid"
                   :routing "routing"
                   :account "account"
                   :amount 120}]
                 paycheck-directives))))

  (it "pays one commissioned employee on an even Friday by Paymaster"
    (let [employees [{:id "empid"
                      :schedule :biweekly
                      :pay-class [:commissioned 100 5/100]
                      :disposition [:paymaster "paymaster"]}]
          sales-receipts {"empid" [["Nov 12 2022" 15000]]}
          db {:employees employees :sales-receipts sales-receipts}
          friday (parse-date "Nov 18 2022")]
      (should (s/valid? ::i/db db))
      (let [paycheck-directives (payroll friday db)]
        (should (s/valid? ::i/paycheck-directives paycheck-directives))
        (should= [{:type :paymaster
                   :id "empid"
                   :paymaster "paymaster"
                   :amount 850}]
                 (payroll friday db)))))

  )

