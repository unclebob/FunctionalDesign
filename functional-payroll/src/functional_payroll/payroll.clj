(ns functional-payroll.payroll
  (:require [functional-payroll.payroll-interface :refer :all]))

(defn create-paycheck-directives [ids payments dispositions]
  (map #(assoc {} :id %1 :amount %2 :disposition %3)
       ids payments dispositions))

(defn get-employees-to-be-paid-today [today employees]
  (filter #(is-today-payday % today) employees))

(defn send-paychecks [ids payments dispositions]
  (for [paycheck-directive (create-paycheck-directives ids payments dispositions)]
    (dispose paycheck-directive)))

(defn get-paycheck-amounts [employees]
  (map calc-pay employees))

(defn get-dispositions [employees]
  (map :disposition employees))

(defn get-ids [employees]
  (map :id employees))

(defn- build-employee [db employee]
  (assoc employee :db db))

(defn get-employees [db]
  (map (partial build-employee db) (:employees db)))

(defn payroll [today db]
  (let [employees (get-employees db)
        employees-to-pay (get-employees-to-be-paid-today today employees)
        amounts (get-paycheck-amounts employees-to-pay)
        ids (get-ids employees)
        dispositions (get-dispositions employees-to-pay)]
    (send-paychecks ids amounts dispositions)))

