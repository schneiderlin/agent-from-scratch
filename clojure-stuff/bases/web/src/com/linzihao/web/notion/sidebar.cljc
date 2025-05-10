(ns com.linzihao.web.notion.sidebar
  (:require
   [com.linzihao.web.generic-components.resize-handle :refer [ResizeHandle]]
   [com.linzihao.web.generic-components.icon :refer [IconButton]]
   [com.linzihao.web.svg.icon :refer [DoubleLeftArrow Compose Home Search Inbox Document]]
   [com.linzihao.web.generic-components.label-button :refer [LabelButton]]
   [com.linzihao.web.generic-components.tree-label :refer [TreeLabel MockChildren]]
   [com.linzihao.web.hooks.hook :refer [Hoverable]]
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   ))

(e/defn CloseSidebarButton [hover? !hide-sidebar?]
  (IconButton
   [(if hover? "opacity-100" "opacity-0")]
   {:tabIndex -1
    :aria-label "Close sidebar"}
   (fn [] (reset! !hide-sidebar? true))
   DoubleLeftArrow))

(e/defn NewPageButton []
  (IconButton
   [] {:tabIndex 0
       :aria-label "New page"}
   (fn [] (println "New page"))
   Compose))

(e/defn SidebarTop [!hide?]
  (let [!hover? (atom false) hover? (e/watch !hover?)]
    (dom/div
     (dom/props {:class "select-none transition-colors ease-in duration-75 cursor-pointer flex items-center min-w-0 h-8 w-auto my-1.5 mx-2 rounded-lg px-0"
                 :style {:user-select "none" :transition "background 20ms ease-in"}})
     (Hoverable !hover?)
     (dom/div (dom/text "Linzihao's workspace"))
     ;; Right side: close sidebar and new page icons
     (dom/div
      (dom/props {:class "flex items-center h-full ml-auto mr-0"})
      (dom/div
       (dom/props {:class "inline-flex items-center mr-0.5 gap-0.5"})
       (CloseSidebarButton hover? !hide?)
       (NewPageButton))))))

(e/defn Tools []
  (dom/div
   (dom/props {:class "my-3"})
   (dom/ul
    (dom/props {:class "flex flex-col"})
    (LabelButton Search "Search")
    (LabelButton Home "Home")
    (LabelButton Inbox "Inbox"))))

(e/defn Foo [page-id !page-id]
  (dom/div
   (TreeLabel Document page-id MockChildren)
   (dom/On "click" (fn [] (reset! !page-id page-id)) nil)))

(e/defn Sidebar [!page-id]
  (let [!width (atom 200) width (e/watch !width)
        !hide? (atom false) hide? (e/watch !hide?)]
    (if hide?
      (dom/text "hide")
      (dom/div
       (dom/props {:id "sidebar"
                   :class "relative border border-gray-300 p-4 min-w-[200px] max-w-[400px] h-screen"
                   :style {:width (str width "px")}})
       (SidebarTop !hide?)
       (Tools)
       (dom/ul
        (Foo "home" !page-id)
        (Foo "Page 2" !page-id)
        (Foo "Page 3" !page-id)
        (Foo "Page 4" !page-id))
       (ResizeHandle 200 400 !width)))))