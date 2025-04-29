(ns com.linzihao.web.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            [com.linzihao.xiangqi.ui.render :refer [Chessboard]]))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     (let [path js/window.location.pathname]
       (if (= path "/xiangqi")
         (Chessboard)
         (dom/div (dom/props {:style {:display "contents"}})
                  (dom/h1 (dom/text "hello world!"))))))))