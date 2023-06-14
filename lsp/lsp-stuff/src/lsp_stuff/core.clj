(ns lsp-stuff.core)

(defn pay [employee pay-date]
  (let [is-payday? (:is-payday employee)
        calc-pay (:calc-pay employee)
        send-paycheck (:send-paycheck employee)]
    (when (= true (is-payday? pay-date))
      (let [paycheck (calc-pay)]
        (send-paycheck paycheck)))))