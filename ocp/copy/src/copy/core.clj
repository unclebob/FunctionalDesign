(ns copy.core)

(defprotocol device
  (getchar [_])
  (putchar [_ c]))

(defn copy [device]
  (let [c (getchar device)]
    (if (= c :eof)
      nil
      (do
        (putchar device c)
        (recur device)))))

(defmulti getchar (fn [device] (:device-type device)))
(defmulti putchar (fn [device c] (:device-type device)))