(ns command.core
  (:require [command.undoable-command :as uc]
            [command.add-room-command :as ar]))

(defn gui-app [actions]
  (loop [actions actions
         undo-list (list)]
    (if (empty? actions)
      :DONE
      (condp = (first actions)
        :add-room-action
        (let [executed-command (uc/execute (ar/make-add-room-command))]
          (recur (rest actions)
                 (conj undo-list executed-command)))

        :undo-action
        (let [command-to-undo (first undo-list)]
          (uc/undo command-to-undo)
          (recur (rest actions)
                 (rest undo-list)))
        :TILT))))
