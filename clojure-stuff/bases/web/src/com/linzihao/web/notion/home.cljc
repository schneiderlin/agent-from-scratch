(ns com.linzihao.web.notion.home
  (:require
   [com.linzihao.web.generic-components.resize-handle :refer [ResizeHandle]]
   [com.linzihao.web.generic-components.icon :refer [IconButton]]
   [com.linzihao.web.svg.icon :refer [DoubleLeftArrow Compose Home Search Inbox Document]]
   [com.linzihao.web.generic-components.label-button :refer [LabelButton]]
   [com.linzihao.web.generic-components.tree-label :refer [TreeLabel MockChildren]]
   [com.linzihao.web.hooks.hook :refer [Hoverable]]
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   #?(:cljs ["@editorjs/editorjs" :as EditorJS])
   #?(:cljs ["@editorjs/header" :as Header])
   #?(:cljs ["@editorjs/checklist" :as Checklist])

   #?(:cljs ["@editorjs/quote" :as Quote])
   #?(:cljs ["@editorjs/list" :as EditorjsList])
   #?(:cljs ["@editorjs/image" :as ImageTool])
   #?(:cljs ["@editorjs/raw" :as RawTool])
  ;;  #?(:cljs ["@editorjs/checklist" :as Checklist])
  ;;  #?(:cljs ["@editorjs/checklist" :as Checklist])
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

(e/defn Sidebar []
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
        (TreeLabel Document "Page 1" MockChildren)
        (TreeLabel Document "Page 2" MockChildren)
        (TreeLabel Document "Page 3" MockChildren)
        (TreeLabel Document "Page 4" MockChildren))
       (ResizeHandle 200 400 !width)))))

#?(:cljs
   (defn debounced-save [api]
     (let [save-data (.save (.-saver api))]
       (.then save-data
              (fn [d]
                (println "saved" d))))))

;; 在 electric 里面, constructor 好像有特殊的含义, 所以要把 new ... 放到普通 clj code 里面
#?(:cljs
   (defn init-editor! [node] 
     (let [editor (new EditorJS (clj->js {:holder node
                                          :onChange (fn [api event]
                                                      (debounced-save api))
                                          :tools {:header {:class Header
                                                           :config {:classNames {1 "text-4xl font-bold my-4",
                                                                                 2 "text-3xl font-semibold my-3",
                                                                                 3 "text-2xl font-medium my-2"}}}
                                                  :checklist {:class Checklist
                                                              :inlineToolbar true}
                                                  :quote {:class Quote
                                                          :inlineToolbar true}
                                                  :list {:class EditorjsList
                                                         :inlineToolbar true}
                                                  :raw RawTool}}))]
       (println "editor" editor))))

(e/defn NotionHome []
  (dom/div
   (dom/props {:class "flex"})
   (Sidebar)
   (let [parent dom/node
         node (dom/div
               (dom/props {:id "editorjs"
                           :class "w-full h-full"}) 
               dom/node)]
     #?(:cljs (set! (.-parent js/window) parent))
     (when (dom/Await-element parent "#editorjs")
       (e/client (init-editor! node)))
     node)))