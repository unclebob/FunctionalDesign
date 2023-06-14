(ns video-store.text-statement_formatter
  (:require [video-store.statement-formatter :refer :all]))

(defn make-text-formatter [] {:type ::text})

(defmethod format-rental-statement ::text [_formatter statement-data]
  (let [customer-name (:customer-name statement-data)
        movies (:movies statement-data)
        owed (:owed statement-data)
        points (:points statement-data)]
    (str
      (format "Rental Record for %s\n" customer-name)
      (apply str
             (for [movie movies]
               (format "\t%s\t%.1f\n" (:title movie) (:price movie))))
      (format "You owed %.1f\n" owed)
      (format "You earned %d frequent renter points\n" points))))