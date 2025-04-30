(ns com.linzihao.web.notion.home
  (:require
   [com.linzihao.web.generic-components.resize-handle :refer [ResizeHandle]]
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]))

(e/defn Sidebar []
  (let [!width (atom 200) width (e/watch !width)]
    (dom/div
     (dom/props {:id "sidebar"
                 :class "relative border border-gray-300 p-4 min-w-[200px] max-w-[400px] h-screen overflow-hidden"
                 :style {:width (str width "px")}})
     (dom/h3
      (dom/props {:class "text-lg font-semibold mb-3"})
      (dom/text "Sidebar"))
     (dom/ul
      (dom/li (dom/props {:class "py-1"}) (dom/text "Page 1"))
      (dom/li (dom/props {:class "py-1"}) (dom/text "Page 2"))
      (dom/li (dom/props {:class "py-1"}) (dom/text "Another Page")))
     (ResizeHandle 200 400 !width))))

(e/defn NotionHome []
  (dom/div
    (dom/props {:class "flex"})
    (Sidebar)
    (dom/div
      (dom/props {:class "p-4 flex-grow"})
      (dom/text "Notion Home"))))