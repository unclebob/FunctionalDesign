(ns word-wrap.core-spec
  (:require [speclj.core :refer :all]
            [word-wrap.core :refer :all]))

(describe "word wrap"
  (it "wraps nothing"
    (should= "" (wrap "" 2)))

  (it "wraps a single character"
    (should= "x" (wrap "x" 2)))

  (it "cuts a long word"
    (should= "x\nx" (wrap "xx" 1)))

  (it "cuts many long words"
    (should= "x\nx\nx" (wrap "xxx" 1)))

  (it "cuts before a space"
    (should= "x\nx" (wrap "x x" 1)))

  (it "cuts at a space"
    (should= "x\nx" (wrap "x x" 2)))

  (it "cuts at a space to preserve a word"
    (should= "x\nxx" (wrap "x xx" 3)))
  )