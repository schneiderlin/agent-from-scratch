(ns com.linzihao.web.generic-components.tree-label
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom] 
   [com.linzihao.web.generic-components.icon :refer [IconButton]]
   [com.linzihao.web.generic-components.rich-label :refer [RichLabel]] 
   [com.linzihao.web.notion.PageOptions :refer [PageOptions]]
   [com.linzihao.web.svg.icon :refer [Chevron Ellipsis Plus Document]]))

(e/declare TreeLabel)

(e/defn StaticChildren []
  (dom/div
   (dom/props {:class "ml-2"})
   (dom/text "child")))

(e/defn RichChildren []
  (RichLabel
   ;; left
   (e/fn [hover?]
     (IconButton [] {}
                 #()
                 Document))
   ;; label
   "label"
   ;; right
   (e/fn [hover?]
     (dom/div
      (dom/props {:class [(if hover? "opacity-100" "opacity-0")]})
      (IconButton []
                  {:tabIndex -1
                   :aria-label "Delete, duplicate, and more…"}
                  #()
                  (e/fn []
                    (dom/div
                     (dom/props {:class "relative"})
                     (Ellipsis))))
      (IconButton []
                  {:tabIndex 0
                   :aria-label "Add a page inside"}
                  (e/fn [])
                  Plus)))
   (e/fn [_])))

(e/defn MockChildren []
  (let [ids (e/diff-by identity (range 10))]
    (dom/ul
     (e/for [id ids]
       (TreeLabel Document (str "SubPage " id) MockChildren)))))

(e/defn TreeLabel [Svg label Children]
  (let [!expand? (atom false) expand? (e/watch !expand?)
        !page-options? (atom false) page-options? (e/watch !page-options?)]
    (RichLabel
     ;; left
     (e/fn [hover?]
       (IconButton [] {}
                   #(swap! !expand? not) 
                   (e/fn []
                     (dom/div
                      (dom/props {:class (if hover? "" "hidden")})
                      (Chevron (if expand? 0 -90)))
                     (dom/div
                      (dom/props {:class (if hover? "hidden" "")})
                      (Svg)))))
     ;; label
     label
     ;; right
     (e/fn [hover?]
       (dom/div
        (dom/props {:class [(if hover? "opacity-100" "opacity-0")]})
        (IconButton []
                    {:tabIndex -1
                     :aria-label "Delete, duplicate, and more…"}
                    #(swap! !page-options? not)
                    (e/fn []
                      (dom/div
                       (dom/props {:class "relative"})
                       (Ellipsis)
                       (when page-options?
                         (PageOptions)))))
        (IconButton []
                    {:tabIndex 0
                     :aria-label "Add a page inside"}
                    (e/fn [])
                    Plus))) 
     (e/fn [_] 
       (when expand? 
         (dom/div
          (dom/props {:class "ml-2"})
          (Children)))))))