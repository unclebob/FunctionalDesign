(ns copy.core-spec
  (:require [speclj.core :refer :all]
            [copy.core :refer :all]))

(defrecord str-device [in-atom out-atom]
  device
  (getchar [_]
    (let [c (first @in-atom)]
      (if (nil? c)
        :eof
        (do
          (swap! in-atom rest)
          c))))

  (putchar [_ c]
    (swap! out-atom str c)))

(describe "copy"
  (it "can read and write using protocol"
    (let [device (->str-device (atom "abcdef") (atom nil))]
      (copy device)
      (should= "abcdef" @(:out-atom device)))))

(defmethod getchar :test-device [device]
  (let [input (:input device)
        c (first @input)]
    (if (nil? c)
      :eof
      (do
        (swap! input rest)
        c))))

(defmethod putchar :test-device [device c]
  (let [output (:output device)]
    (swap! output str c)))

(describe "copy-mm"
  (it "can read and write using multi-method"
    (let [device {:device-type :test-device
                  :input (atom "abcdef")
                  :output (atom nil)}]
      (copy device)
      (should= "abcdef" @(:output device)))))