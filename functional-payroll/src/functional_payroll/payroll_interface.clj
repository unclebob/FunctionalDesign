(ns functional-payroll.payroll-interface
  (:require [clojure.spec.alpha :as s]))

(defn- get-pay-class [employee]
  (first (:pay-class employee)))

(defn- get-disposition [paycheck-directive]
  (first (:disposition paycheck-directive)))

(defmulti is-today-payday :schedule)
(defmulti calc-pay get-pay-class)
(defmulti dispose get-disposition)

(s/def ::id string?)
(s/def ::schedule #{:monthly :weekly :biweekly})
(s/def ::salaried-pay-class (s/tuple #(= % :salaried) pos?))
(s/def ::hourly-pay-class (s/tuple #(= % :hourly) pos?))
(s/def ::commissioned-pay-class (s/tuple #(= % :commissioned) pos? pos?))
(s/def ::pay-class (s/or :salaried ::salaried-pay-class
                         :hourly ::hourly-pay-class
                         :commissioned ::commissioned-pay-class))

(s/def ::mail-disposition (s/tuple #(= % :mail) string? string?))
(s/def ::deposit-disposition (s/tuple #(= % :deposit) string? string?))
(s/def ::paymaster-disposition (s/tuple #(= % :paymaster) string?))
(s/def ::disposition (s/or :mail ::mail-disposition
                           :deposit ::deposit-disposition
                           :paymaster ::paymaster-disposition))

(s/def ::employee (s/keys :req-un [::id ::schedule ::pay-class ::disposition]))
(s/def ::employees (s/coll-of ::employee))

(s/def ::date string?)
(s/def ::time-card (s/tuple ::date pos?))
(s/def ::time-cards (s/map-of ::id (s/coll-of ::time-card)))

(s/def ::sales-receipt (s/tuple ::date pos?))
(s/def ::sales-receipts (s/map-of ::id (s/coll-of ::sales-receipt)))

(s/def ::db (s/keys :req-un [::employees]
                    :opt-un [::time-cards ::sales-receipts]))

(s/def ::amount pos?)
(s/def ::name string?)
(s/def ::address string?)
(s/def ::mail-directive (s/and #(= (:type %) :mail)
                               (s/keys :req-un [::id ::name ::address ::amount])))

(s/def ::routing string?)
(s/def ::account string?)
(s/def ::deposit-directive (s/and #(= (:type %) :deposit)
                                  (s/keys :req-un [::id ::routing ::account ::amount])))

(s/def ::paymaster string?)
(s/def ::paymaster-directive (s/and #(= (:type %) :paymaster)
                                    (s/keys :req-un [::id ::paymaster ::amount])))

(s/def ::paycheck-directive (s/or :mail ::mail-directive
                                  :deposit ::deposit-directive
                                  :paymaster ::paymaster-directive))

(s/def ::paycheck-directives (s/coll-of ::paycheck-directive))



