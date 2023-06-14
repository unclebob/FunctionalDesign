(ns command.add-room-command
  (:require [command.undoable-command :as uc]))

(defn add-room []
  ;stuff that adds rooms to the canvas
  ;and returns the added room
  )

(defn delete-room [room]
  ;stuff that deletes the specified room from the canvas
  )

(defn make-add-room-command []
  {:type :add-room-command})

(defmethod uc/execute :add-room-command [command]
  (assoc (make-add-room-command) :the-added-room (add-room)))

(defmethod uc/undo :add-room-command [command]
  (delete-room (:the-added-room command)))