(ns telco-simulator.core-spec
  (:require [speclj.core :refer :all]
            [telco-simulator.core :refer :all]))

(describe "telco"
  (it "should make and receive call"
    (let [caller (make-user-agent "Bob")
          callee (make-user-agent "Alice")
          telco (make-telco-agent "telco")]
      (reset! log [])
      (send caller transition :call [telco caller callee])
      (Thread/sleep 100)
      (prn @log)
      (should= :idle (:state @caller))
      (should= :idle (:state @callee))
      (should= :idle (:state @telco))
      ))

  (it "should race"
    (let [caller (make-user-agent "Bob")
          callee (make-user-agent "Alice")
          telco1 (make-telco-agent "telco1")
          telco2 (make-telco-agent "telco2")]
      (reset! log [])
      (send caller transition :call [telco1 caller callee])
      (Thread/sleep 5)
      (send callee transition :call [telco2 callee caller])
      (Thread/sleep 100)
      (prn @log)
      (should= :idle (:state @caller))
      (should= :idle (:state @callee))
      (should= :idle (:state @telco1))
      (should= :idle (:state @telco2))))
  )