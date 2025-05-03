(ns com.linzihao.web.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [missionary.core :as m]
            #_[com.linzihao.xiangqi.ui.render :refer [Chessboard]]
            [com.linzihao.web.notion.home :refer [NotionHome]]))

#?(:clj (defonce a (atom 1)))
#?(:clj (def flow (m/watch a)))


(comment
  (swap! a inc)
  :rcf)

(e/defn Test [] 
  (let [value (e/server (e/input flow))]
    (dom/div (dom/text value))))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     (let [path js/window.location.pathname]
       (case path
         "/notion" (NotionHome)
       #_#_  "/xiangqi" (Chessboard)
         (Test))))))