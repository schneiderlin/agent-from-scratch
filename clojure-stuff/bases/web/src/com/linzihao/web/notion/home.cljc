(ns com.linzihao.web.notion.home
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn Sidebar []
  (dom/div
    (dom/props {:class "border border-gray-300 p-4 min-w-[200px] h-screen"})
    (dom/h3 
      (dom/props {:class "text-lg font-semibold mb-3"})
      (dom/text "Sidebar"))
    (dom/ul
      (dom/li (dom/props {:class "py-1"}) (dom/text "Page 1"))
      (dom/li (dom/props {:class "py-1"}) (dom/text "Page 2"))
      (dom/li (dom/props {:class "py-1"}) (dom/text "Another Page")))))

(e/defn NotionHome []
  (dom/div 
    (dom/props {:class "flex"})
    (Sidebar)
    (dom/div
      (dom/props {:class "p-4 flex-grow"})
      (dom/text "Notion Home"))))