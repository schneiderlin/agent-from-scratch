(ns com.linzihao.web.notion.home
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn Sidebar []
  (let [!width (atom 200) width (e/watch !width) 
        !start-info (atom nil) start-info (e/watch !start-info)]
    (letfn [(handle-mouse-move [event]
              (let [{:keys [start-x start-width]} start-info
                    current-x (.-clientX event)
                    dx (- current-x start-x)
                    new-width (+ start-width dx)
                    clamped-width (max 200 (min new-width 400))]
                (reset! !width clamped-width)
                (.preventDefault event)))
            (handle-mouse-up [event] 
              (reset! !start-info nil)
              (.preventDefault event)
              (.removeEventListener js/document "mousemove" handle-mouse-move)
              (.removeEventListener js/document "mouseup" handle-mouse-up))]
      (e/on-unmount (fn []
                      (.removeEventListener js/document "mousemove" handle-mouse-move)
                      (.removeEventListener js/document "mouseup" handle-mouse-up)))
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
        (dom/On "mousedown" (fn [e] 
                              (reset! !start-info {:start-x (.-clientX e)
                                                   :start-width width})
                              (.preventDefault e) ; Prevent text selection
                              (.addEventListener js/document "mousemove" handle-mouse-move)
                              (.addEventListener js/document "mouseup" handle-mouse-up)) nil))))))

(e/defn NotionHome []
  (dom/div
    (dom/props {:class "flex"})
    (Sidebar)
    (dom/div
      (dom/props {:class "p-4 flex-grow"})
      (dom/text "Notion Home"))))