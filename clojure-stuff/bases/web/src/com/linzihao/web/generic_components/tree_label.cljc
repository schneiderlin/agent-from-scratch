(ns com.linzihao.web.generic-components.tree-label
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom] 
   [com.linzihao.web.hooks.hook :refer [Hoverable]] 
   [com.linzihao.web.svg.icon :refer [Chevron Ellipsis Plus Document]]))

(e/declare TreeLabel)

(e/defn MockChildren []
  (dom/ul
   (TreeLabel Document "SubPage 1" MockChildren)
   (TreeLabel Document "SubPage 2" MockChildren)
   (TreeLabel Document "SubPage 3" MockChildren)))

(e/defn TreeLabel [Svg label Children]
  (let [!hover? (atom false) hover? (e/watch !hover?)
        !expand? (atom false) expand? (e/watch !expand?)]
    (dom/div
     (dom/props {:class "flex items-center gap-2 py-1 rounded-md cursor-pointer hover:bg-gray-100 justify-start text-left select-none"})
     (Hoverable !hover?)
     (dom/On "click" #(swap! !expand? not) nil)
     ;; Left icon/chevron
     (if hover?
       (Chevron (if expand? 0 -90))
       (Svg))
     ;; Label text
     (dom/span
      (dom/props {:class "flex-1 overflow-hidden whitespace-nowrap text-ellipsis"})
      (dom/text label))
     ;; Right action icons
     (dom/div
      (dom/props {:class [(if hover? "opacity-100" "opacity-100") "flex items-center justify-center ml-auto"]})
      (dom/div
       (dom/props {:role "button" :tabIndex 0 :aria-label "Delete, duplicate, and more…"
                   :class "select-none transition-colors duration-75 cursor-pointer flex items-center justify-center w-5 h-5 rounded-md ml-1"})
       (Ellipsis))
      (dom/div
       (dom/props {:role "button" :tabIndex 0 :aria-label "Add a page inside"
                   :class "select-none transition-colors duration-75 cursor-pointer flex items-center justify-center w-5 h-5 rounded-md ml-1"})
       (Plus))))
    ;; Render children if expanded
    (when expand?
      (dom/div
       (dom/props {:class "ml-2"})
       (Children)))))