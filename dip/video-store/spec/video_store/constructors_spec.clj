(ns video-store.constructors-spec
  (:require [speclj.core :refer :all]
            [video-store.constructors :refer :all :as constructors]
            [clojure.spec.alpha :as s]))

(describe "constructors"
  (it "creates valid customer"
    (should-be-nil
      (s/explain-data
        ::constructors/customer
        (make-customer "CUSTOMER"))))

  (it "creates valid movie"
    (should-be-nil
      (s/explain-data
        ::constructors/movie
        (make-movie "title" :regular))))

  (it "creates valid rental"
    (should-be-nil
      (s/explain-data
        ::constructors/rental
        (make-rental (make-movie "title" :new-release)
                     32))))

  (it "creates valid rental orders"
    (should-be-nil
      (s/explain-data
        ::constructors/rental-order
        (make-rental-order
          (make-customer "CUSTOMER")
          [(make-rental
             (make-movie "title" :regular) 3)])))))
