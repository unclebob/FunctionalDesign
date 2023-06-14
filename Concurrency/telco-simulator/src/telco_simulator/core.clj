(ns telco-simulator.core)

(def log (atom []))

(declare transition)

(defn caller-off-hook [sm-agent [telco caller callee :as call-data]]
  (swap! log conj (str (:name @caller) " goes off hook."))
  (send-off telco transition :caller-off-hook call-data))

(defn dial [sm-agent [telco caller callee :as call-data]]
  (swap! log conj (str (:name @caller) " dials"))
  (send-off telco transition :dial call-data))

(defn callee-off-hook [sm-agent [telco caller callee :as call-data]]
  (swap! log conj (str (:name @callee) " goes off hook"))
  (send-off telco transition :callee-off-hook call-data))

(defn talk [sm-agent [telco caller callee :as call-data]]
  (swap! log conj (str (:name sm-agent) " talks."))
  (Thread/sleep 10)
  (swap! log conj (str (:name sm-agent) " hangs up."))
  (send-off telco transition :hangup call-data))

(defn dialtone [sm-agent [telco caller callee :as call-data]]
  (Thread/sleep 10)
  (swap! log conj (str "dialtone to " (:name @caller)))
  (send-off caller transition :dialtone call-data))

(defn ring [sm-agent [telco caller callee :as call-data]]
  (swap! log conj (str "telco rings " (:name @callee)))
  (send callee transition :ring call-data)
  (send-off caller transition :ringback call-data))

(defn connect [sm-agent [telco caller callee :as call-data]]
  (swap! log conj "telco connects")
  (send-off caller transition :connected call-data)
  (send-off callee transition :connected call-data))

(defn disconnect [sm-agent [telco caller callee :as call-data]]
  (swap! log conj "disconnect")
  (send-off callee transition :disconnect call-data)
  (send-off caller transition :disconnect call-data))

(def user-sm
  {:idle {:call [:calling caller-off-hook]
          :ring [:waiting-for-connection callee-off-hook]
          :disconnect [:idle nil]}
   :calling {:dialtone [:dialing dial]}
   :dialing {:ringback [:waiting-for-connection nil]}
   :waiting-for-connection {:connected [:talking talk]}
   :talking {:disconnect [:idle nil]}})

(def telco-sm
  {:idle {:caller-off-hook [:waiting-for-dial dialtone]
          :hangup [:idle nil]}
   :waiting-for-dial {:dial [:waiting-for-answer ring]}
   :waiting-for-answer {:callee-off-hook [:waiting-for-hangup connect]}
   :waiting-for-hangup {:hangup [:idle disconnect]}})

(defn make-user-agent [name]
  (agent {:state :idle :name name :machine user-sm}))

(defn make-telco-agent [name]
  (agent {:state :idle :name name :machine telco-sm}))

(defn transition [machine-agent event event-data]
  (swap! log conj (str (:name machine-agent) "<-" event))
  (let [state (:state machine-agent)
        sm (:machine machine-agent)
        result (get-in sm [state event])]
    (if (nil? result)
      (do
        (swap! log conj "TILT!")
        machine-agent)
      (do
        (when (second result) ((second result) machine-agent event-data))
        (assoc machine-agent :state (first result))))))
