(ns com.linzihao.web.notion.page
  (:require 
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   #?(:clj [com.linzihao.web.db.db :refer [conn upsert! get-blocks-in-page]])
   #?(:cljs ["@editorjs/editorjs" :as EditorJS])
   #?(:cljs ["@editorjs/header" :as Header])
   #?(:cljs ["@editorjs/checklist" :as Checklist])
   #?(:cljs ["@editorjs/quote" :as Quote])
   #?(:cljs ["@editorjs/list" :as EditorjsList])
   #?(:cljs ["@editorjs/raw" :as RawTool])))

#?(:cljs
   (do
     (defonce debounce-timeout (atom nil))

     (defn debounced-save [api page-id !upsert-queue]
       (when @debounce-timeout
         (js/clearTimeout @debounce-timeout))
       (reset! debounce-timeout
               (js/setTimeout
                (fn []
                  (println "timeout")
                  (let [save-data (.save (.-saver api))]
                    (.then save-data
                           (fn [d]
                             (let [d (js->clj d :keywordize-keys true)
                                   blocks (:blocks d)
                                   block-ids (map :id blocks)
                                   page-entity {:block/id page-id :block/type "page" :block/data {:blocks (vec block-ids)}}
                                   upsert-blocks (conj
                                                  (map (fn [block] {:block/id (:id block)
                                                                    :block/type (:type block)
                                                                    :block/data (:data block)}) blocks)
                                                  page-entity)]
                               (swap! !upsert-queue concat upsert-blocks))))))
                5000)))))

#?(:cljs
   (defn init-editor! [node page-id initial-data !upsert-queue]
     (println "init-editor!" initial-data)
     (let [editor (new EditorJS (clj->js (merge
                                          {:holder node
                                           :onChange (fn [api event]
                                                       (debounced-save api page-id !upsert-queue))
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

(e/defn Page [id]
  (let [parent dom/node
        node (dom/div
              (dom/props {:id "editorjs"
                          :class "w-full h-full"})
              dom/node)
        !upsert-queue (atom []) upsert-queue (e/watch !upsert-queue)
        blocks (e/server (get-blocks-in-page conn id))
        initial-data (when (seq blocks) {:blocks (clj->js blocks)})]
    #_(println "blocks" blocks)
    (e/server
     (println "to save " upsert-queue)
     (for [entity upsert-queue]
       (upsert! conn entity))
     nil)
    (when (dom/Await-element parent "#editorjs")
      (e/client (init-editor! node id initial-data !upsert-queue)))
    node))