(ns com.linzihao.web.notion.home
  (:require 
   [com.linzihao.web.notion.sidebar :refer [Sidebar]] 
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   #?(:cljs ["@editorjs/editorjs" :as EditorJS])
   #?(:cljs ["@editorjs/header" :as Header])
   #?(:cljs ["@editorjs/checklist" :as Checklist])

   #?(:cljs ["@editorjs/quote" :as Quote])
   #?(:cljs ["@editorjs/list" :as EditorjsList]) 
   #?(:cljs ["@editorjs/raw" :as RawTool])
   ))

#?(:cljs
   (do
     (defonce debounce-timeout (atom nil))
     (defn debounced-save [api]
       (when @debounce-timeout
         (js/clearTimeout @debounce-timeout))
       (reset! debounce-timeout
               (js/setTimeout
                (fn []
                  (let [save-data (.save (.-saver api))]
                    (.then save-data
                           (fn [d]
                             (js/localStorage.setItem "editor-content" (js/JSON.stringify d))
                             (println "saved and stored to localStorage" d)))))
                500)))))

;; 在 electric 里面, constructor 好像有特殊的含义, 所以要把 new ... 放到普通 clj code 里面
#?(:cljs
   (defn init-editor! [node]
     (let [maybe-data (js/localStorage.getItem "editor-content")
           initial-data (when maybe-data (js/JSON.parse maybe-data))
           editor (new EditorJS (clj->js (merge
                                          {:holder node
                                           :onChange (fn [api event]
                                                       (debounced-save api))
                                           :tools {:header {:class Header
                                                            :config {:classNames {1 "text-4xl font-bold my-4"
                                                                                  2 "text-3xl font-semibold my-3"
                                                                                  3 "text-2xl font-medium my-2"}}}
                                                   :checklist {:class Checklist
                                                               :inlineToolbar true}
                                                   :quote {:class Quote
                                                           :inlineToolbar true}
                                                   :list {:class EditorjsList
                                                          :inlineToolbar true}
                                                   :raw RawTool}}
                                          (when initial-data {:data initial-data}))))]
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