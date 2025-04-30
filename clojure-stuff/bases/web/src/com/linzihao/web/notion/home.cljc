(ns com.linzihao.web.notion.home
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

;; 不让外部管理 width, sidebar 自己藏起来, 所以参数不传 width
(e/defn Sidebar []
  (let [!width (atom 200) width (e/watch !width)
        !is-resizing (atom false) is-resizing (e/watch !is-resizing)
        !start-info (atom nil) start-info (e/watch !start-info)] ; Store {:start-x number, :start-width number}
    (dom/div
     (dom/props {:id "sidebar"
                 :class "relative border border-gray-300 p-4 min-w-[200px] max-w-[400px] h-screen overflow-hidden"
                 :style {:width (str width "px")}})
     (dom/h3
      (dom/props {:class "text-lg font-semibold mb-3"})
      (dom/text "Sidebar"))
     (dom/ul
      (dom/li (dom/props {:class "py-1"}) (dom/text "Page 1"))
      (dom/li (dom/props {:class "py-1"}) (dom/text "Page 2"))
      (dom/li (dom/props {:class "py-1"}) (dom/text "Another Page")))
     (dom/div
      (dom/props {:class "absolute top-0 right-0 bottom-0 w-0.5 bg-transparent hover:bg-gray-400 cursor-col-resize select-none"})
      ;; Mouse Down: Start resizing
      (dom/On "mousedown" (fn [e]
                            (println "mousedown")
                            (reset! !is-resizing true)
                            (reset! !start-info {:start-x (.-clientX e)
                                                 :start-width width})
                            (.preventDefault e) ; Prevent text selection
                            )nil))
     
     (dom/On "mousemove" (fn [e]
                           (when is-resizing ; Check atom directly
                             (println "mousemove")
                             (when-let [{:keys [start-x start-width]} start-info]
                               (let [current-x (.-clientX e)
                                     delta-x (- current-x start-x)
                                     new-width (+ start-width delta-x)]
                                 (println "new-width" new-width)
                                 (reset! !width (max 200 (min 400 new-width))))))) nil)
     (dom/On "mouseup" (fn [_e]
                         (println "mouseup")
                         (when is-resizing ; Check atom directly
                           (reset! !is-resizing false)
                           (reset! !start-info nil))) nil))))

(e/defn NotionHome []
  (dom/div
    (dom/props {:class "flex"})
    (Sidebar)
    (dom/div
      (dom/props {:class "p-4 flex-grow"})
      (dom/text "Notion Home"))))