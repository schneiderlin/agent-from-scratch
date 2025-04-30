(ns com.linzihao.web.generic-components.tree-label
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom] 
   [com.linzihao.web.hooks.hook :refer [Hoverable]]
   [com.linzihao.web.svg.icon :refer [Chevron Ellipsis Plus]]))

(e/defn TreeLabel [Svg label]
  (let [!hover? (atom false) hover? (e/watch !hover?)]
    (dom/div
     (dom/props {:class "flex items-center gap-2 py-1 rounded-md cursor-pointer hover:bg-gray-100 justify-start text-left"})
     (Hoverable !hover?)
     ;; Left icon/chevron
     (if hover?
       (Chevron)
       (Svg))
     ;; Label text
     (dom/span (dom/text label))
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
       (Plus))))))