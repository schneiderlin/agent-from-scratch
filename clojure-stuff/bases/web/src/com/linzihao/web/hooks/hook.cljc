(ns com.linzihao.web.hooks.hook
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn Hoverable [!hover?]
  (dom/On "mouseenter" #(reset! !hover? true) nil)
  (dom/On "mouseleave" #(reset! !hover? false) nil))