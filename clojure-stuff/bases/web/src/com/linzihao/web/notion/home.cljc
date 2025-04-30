(ns com.linzihao.web.notion.home
  (:require
   [com.linzihao.web.generic-components.resize-handle :refer [ResizeHandle]]
   [com.linzihao.web.generic-components.icon :refer [Icon IconButton]]
   [com.linzihao.web.generic-components.label-button :refer [LabelButton]]
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   [hyperfiddle.electric-svg3 :as svg]))

(e/defn CloseSidebarButton [hover? !hide-sidebar?]
  (IconButton
   [(if hover? "opacity-100" "opacity-0")]
   {:tabIndex -1
    :aria-label "Close sidebar"}
   (fn [] (reset! !hide-sidebar? true))
   (e/fn []
     (svg/svg
      (dom/props {:aria-hidden "true"
                  :role "graphics-symbol"
                  :viewBox "0 0 20 20"
                  :class "arrowChevronDoubleBackward"
                  :style {:width "22px" :height "22px" :display "block" :fill "inherit" :flex-shrink 0}})
      (svg/path (dom/props {:d "M3.608 10.442a.625.625 0 0 1 0-.884l5.4-5.4a.625.625 0 0 1 .884.884L4.934 10l4.958 4.958a.625.625 0 1 1-.884.884z"}))
      (svg/path (dom/props {:d "m14.508 4.158-5.4 5.4a.625.625 0 0 0 0 .884l5.4 5.4a.625.625 0 1 0 .884-.884L10.434 10l4.958-4.958a.625.625 0 1 0-.884-.884"}))))))

(e/defn NewPageButton []
  (IconButton
   [] {:tabIndex 0
       :aria-label "New page"}
   (fn [] (println "New page"))
   (e/fn []
     (svg/svg
      (dom/props {:aria-hidden "true"
                  :role "graphics-symbol"
                  :viewBox "0 0 20 20"
                  :class "compose"
                  :style {:width "22px" :height "22px" :display "block" :fill "#322C2C" :flex-shrink 0}})
      (svg/path (dom/props {:d "m16.774 4.341-.59.589-1.109-1.11.596-.594a.784.784 0 0 1 1.103 0c.302.302.302.8 0 1.102zM8.65 12.462l6.816-6.813-1.11-1.11-6.822 6.808a1.1 1.1 0 0 0-.236.393l-.289.932c-.052.196.131.38.315.314l.932-.288a.9.9 0 0 0 .394-.236"}))
      (svg/path (dom/props {:d "M4.375 6.25c0-1.036.84-1.875 1.875-1.875H11a.625.625 0 1 0 0-1.25H6.25A3.125 3.125 0 0 0 3.125 6.25v7.5c0 1.726 1.4 3.125 3.125 3.125h7.5c1.726 0 3.125-1.4 3.125-3.125V9a.625.625 0 1 0-1.25 0v4.75c0 1.036-.84 1.875-1.875 1.875h-7.5a1.875 1.875 0 0 1-1.875-1.875z"}))))))

(e/defn SidebarTop [!hide?]
  (let [!hover? (atom false) hover? (e/watch !hover?)] 
    (dom/div
     (dom/props {:class "select-none transition-colors ease-in duration-75 cursor-pointer flex items-center min-w-0 h-8 w-auto my-1.5 mx-2 rounded-lg px-0"
                 :style {:user-select "none" :transition "background 20ms ease-in"}})
     (dom/On "mouseover" #(reset! !hover? true) nil)
     (dom/On "mouseleave" #(reset! !hover? false) nil)
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
    (LabelButton
     (e/fn []
       (svg/svg
        (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
        (svg/path (dom/props {:d "M8.5 3.5a5 5 0 1 1 0 10 5 5 0 0 1 0-10zm7 13-3.5-3.5" :strokeWidth "1.5" :strokeLinecap "round" :strokeLinejoin "round"}))))
     "Search")
    (LabelButton
     (e/fn []
       (svg/svg
        (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
        (svg/path (dom/props {:d "M3 9.5 10 4l7 5.5V16a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z" :strokeWidth "1.5" :strokeLinejoin "round"}))))
     "Home")
    (LabelButton
     (e/fn []
       (svg/svg
        (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
        (svg/path (dom/props {:d "M3.5 6.5v7a2 2 0 0 0 2 2h9a2 2 0 0 0 2-2v-7a2 2 0 0 0-2-2h-9a2 2 0 0 0-2 2zm0 7 3.5-3 2.5 2 2.5-2 3.5 3" :strokeWidth "1.5" :strokeLinejoin "round"}))))
     "Inbox"))))

(e/defn Sidebar []
  (let [!width (atom 200) width (e/watch !width)
        !hide? (atom false) hide? (e/watch !hide?)]
    (if hide?
      (dom/text "hide")
      (dom/div
       (dom/props {:id "sidebar"
                   :class "relative border border-gray-300 p-4 min-w-[200px] max-w-[400px] h-screen overflow-hidden"
                   :style {:width (str width "px")}})
       (SidebarTop !hide?)
       (Tools)
       (dom/ul
        (dom/li (dom/props {:class "py-1"}) (dom/text "Page 1"))
        (dom/li (dom/props {:class "py-1"}) (dom/text "Page 2"))
        (dom/li (dom/props {:class "py-1"}) (dom/text "Another Page")))
       (ResizeHandle 200 400 !width)))))

(e/defn NotionHome []
  (dom/div
    (dom/props {:class "flex"})
    (Sidebar)
    (dom/div
      (dom/props {:class "p-4 flex-grow"})
      (dom/text "Notion Home"))))