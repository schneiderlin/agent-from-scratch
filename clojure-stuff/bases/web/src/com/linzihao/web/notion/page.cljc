(ns com.linzihao.web.notion.page
  (:require 
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   #?(:clj [com.linzihao.web.db.db :refer [conn page-upsert! get-blocks-in-page]])
   #?(:cljs ["@editorjs/editorjs" :as EditorJS])
   #?(:cljs ["@editorjs/header" :as Header])
   #?(:cljs ["@editorjs/checklist" :as Checklist])
   #?(:cljs ["@editorjs/quote" :as Quote])
   #?(:cljs ["@editorjs/list" :as EditorjsList])
   #?(:cljs ["@editorjs/raw" :as RawTool])))

#?(:cljs
   (do
     (defonce debounce-timeout (atom nil))

     (defn debounced-save [api !upsert-queue]
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
                                   upsert-blocks (map (fn [block] {:block/id (:id block)
                                                                   :block/type (:type block)
                                                                   :block/data (:data block)}) blocks)]
                               (swap! !upsert-queue concat upsert-blocks))))))
                5000)))))

#?(:cljs
   (defn init-editor! [node !upsert-queue !data]
     (println "init editor")
     (let [^js editor
           (new EditorJS (clj->js {:holder node
                                      :onChange (fn [api event]
                                                  (debounced-save api !upsert-queue))
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
                                              :raw RawTool}}))]
       (.then (.-isReady editor)
              (fn []
                (let [blocks (.-blocks editor)]
                  (when (seq @!data)
                    (println "initial data render" @!data)
                    (.render blocks (clj->js @!data)))
                  (add-watch !data :key
                             (fn [_ _ _ new-data]
                               (println "new-data" new-data)
                               (if new-data
                                 (.render blocks (clj->js new-data))
                                 (.clear blocks))))))))))

(e/defn Page [id]
  (let [parent dom/node
        node (dom/div
              (dom/props {:id "editorjs"
                          :class "w-full h-full"})
              dom/node)
        !upsert-queue (atom []) upsert-queue (e/watch !upsert-queue)
        blocks (e/server (get-blocks-in-page conn id))
        data (when (seq blocks) {:blocks blocks})
        !data (atom nil)]
    (reset! !data data)
    (e/server
     (println "to save " upsert-queue)
     (when (seq upsert-queue)
       (page-upsert! conn id upsert-queue)
       (e/client (reset! !upsert-queue []))) 
     nil)
    (when (dom/Await-element parent "#editorjs")
      (e/client 
       (init-editor! node !upsert-queue !data))) 
    node))