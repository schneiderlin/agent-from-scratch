(ns com.linzihao.web.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [com.linzihao.web.mvp.mvp :refer [Menu]] 
            [com.linzihao.xiangqi.ui.render :refer [Chessboard]]
            [com.linzihao.web.notion.home :refer [NotionHome]]))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     (let [path js/window.location.pathname]
       (case path
         "/notion" (NotionHome)
         "/xiangqi" (Chessboard)
         (Menu))))))
