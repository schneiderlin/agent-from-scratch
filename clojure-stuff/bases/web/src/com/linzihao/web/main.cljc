(ns com.linzihao.web.main
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn Main [ring-request]
  (e/client
   (binding [dom/node js/document.body
             e/http-request (e/server ring-request)]
     (dom/div (dom/props {:style {:display "contents"}})
              (dom/h1 (dom/text "hello world!"))))))