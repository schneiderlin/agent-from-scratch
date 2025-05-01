(ns com.linzihao.web.generic-components.tree-label
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom] 
   [com.linzihao.web.generic-components.icon :refer [IconButton]]
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
     ;; Left icon/chevron
     (IconButton [] {}
                 #(swap! !expand? not)
                 (e/fn []
                   (if hover?
                     (Chevron (if expand? 0 -90))
                     (Svg))))
     ;; Label text
     (dom/span
      (dom/props {:class "flex-1 overflow-hidden whitespace-nowrap text-ellipsis"})
      (dom/text label))
     ;; Right action icons
     (dom/div
      (dom/props {:class [(if hover? "opacity-100" "opacity-0") "flex items-center justify-center ml-auto"]})
      (IconButton []
                  {:tabIndex -1
                   :aria-label "Delete, duplicate, and more…"}
                  (e/fn [])
                  Ellipsis)
      (IconButton []
                  {:tabIndex 0
                   :aria-label "Add a page inside"}
                  (e/fn [])
                  Plus)))
    ;; Render children if expanded
    (when expand?
      (dom/div
       (dom/props {:class "ml-2"})
       (Children)))))