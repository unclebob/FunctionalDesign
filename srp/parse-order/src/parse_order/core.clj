(ns parse-order.core
  (:require [clojure.spec.alpha :as s]))

(s/def ::id (s/and
              string?
              #(re-matches #"\d+" %)))
(s/def ::name string?)
(s/def ::address string?)
(s/def ::credit-limit (s/and int? #(<= % 50000)))
(s/def ::customer (s/keys :req-un [::id ::name ::address ::credit-limit]))


(defn validate-customer [{:keys [id name address credit-limit] :as customer}]
  (if (or (nil? id)
          (nil? name)
          (nil? address)
          (nil? credit-limit))
    :invalid
    (let [credit-limit (Integer/parseInt credit-limit)]
      (if (> credit-limit 50000)
        :invalid
        (assoc customer :credit-limit credit-limit)))))

(defn parse-customer [lines]
  (let [[_ id] (re-matches #"^Customer-id: (\d{7})$" (nth lines 0))
        [_ name] (re-matches #"^Name: (.+)$" (nth lines 1))
        [_ address] (re-matches #"^Address: (.+)$" (nth lines 2))
        [_ credit-limit] (re-matches #"^Credit Limit: (\d+)$" (nth lines 3))]
    (validate-customer
      {:id id
       :name name
       :address address
       :credit-limit credit-limit})))


