(ns command.core-spec
  (:require [speclj.core :refer :all]
            [command.core :refer :all]
            [command.add-room-command :as ar]))

(describe "command"
  (with-stubs)
  (it "executes the command"
    (with-redefs [ar/add-room (stub :add-room {:return :a-room})
                  ar/delete-room (stub :delete-room)]
      (gui-app [:add-room-action :undo-action])
      (should-have-invoked :add-room)
      (should-have-invoked :delete-room {:with [:a-room]}))))
