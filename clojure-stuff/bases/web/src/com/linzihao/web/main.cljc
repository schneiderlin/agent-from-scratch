(ns com.linzihao.web.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [missionary.core :as m]
            [com.linzihao.xiangqi.ui.render :refer [Chessboard]]))

#?(:clj (defonce a (atom 1)))

(comment
  (swap! a inc)
  :rcf)

(e/defn Test [] 
  (let [flow (m/watch (atom 0))
        value (new flow)]
    (dom/div (dom/text value))))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     (let [path js/window.location.pathname]
       (if (= path "/xiangqi")
         (Chessboard)
         (Test))))))