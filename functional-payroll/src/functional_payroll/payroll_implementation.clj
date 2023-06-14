(ns functional-payroll.payroll-implementation
  (:require [functional-payroll.payroll-interface :refer [is-today-payday
                                                          calc-pay
                                                          dispose]]))

(defn- is-last-day-of-month [date]
  true)

(defmethod is-today-payday :monthly [employee today]
  (is-last-day-of-month today))

(defn- is-friday [date]
  true)

(defmethod is-today-payday :weekly [employee today]
  (is-friday today))

(defn- is-even-friday [today]
  true)

(defmethod is-today-payday :biweekly [employee today]
  (is-even-friday today))

(defn- get-salary [employee]
  (second (:pay-class employee)))

(defmethod calc-pay :salaried [employee]
  (get-salary employee))

(defmethod calc-pay :hourly [employee]
  (let [db (:db employee)
        time-cards (:time-cards db)
        my-time-cards (get time-cards (:id employee))
        [_ hourly-rate] (:pay-class employee)
        hours (map second my-time-cards)
        total-hours (reduce + hours)]
    (* total-hours hourly-rate)))

(defmethod calc-pay :commissioned [employee]
  (let [db (:db employee)
        sales-receipts (:sales-receipts db)
        my-sales-receipts (get sales-receipts (:id employee))
        [_ base-pay commission-rate] (:pay-class employee)
        sales (map second my-sales-receipts)
        total-sales (reduce + sales)]
    (+ (* total-sales commission-rate) base-pay)))

(defmethod dispose :mail [{:keys [id amount disposition]}]
  {:type :mail
   :id id
   :name (nth disposition 1)
   :address (nth disposition 2)
   :amount amount})

(defmethod dispose :deposit [{:keys [id amount disposition]}]
  {:type :deposit
   :id id
   :routing (nth disposition 1)
   :account (nth disposition 2)
   :amount amount})

(defmethod dispose :paymaster [{:keys [id amount disposition]}]
  {:type :paymaster
   :id id
   :amount amount
   :paymaster (nth disposition 1)}
  )

