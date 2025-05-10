(ns com.linzihao.web.notion.home
  (:require 
   [com.linzihao.web.notion.sidebar :refer [Sidebar]] 
   [com.linzihao.web.notion.page :refer [Page]]
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   ))

(e/defn NotionHome []
  (let [!page-id (atom "home") page-id (e/watch !page-id)]
    (dom/div
     (dom/props {:class "flex"})
     (Sidebar !page-id)
     (Page page-id))))