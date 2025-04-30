(ns com.linzihao.web.notion.home
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn NotionHome []
  (dom/div 
   (dom/text "Notion Home")))