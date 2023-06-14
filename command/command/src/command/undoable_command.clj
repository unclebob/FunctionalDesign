(ns command.undoable-command)

(defmulti execute :type)
(defmulti undo :type)
