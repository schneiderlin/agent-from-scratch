(ns com.linzihao.web.generic-components.resize-handle
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn ResizeHandle [min-width max-width !width]
  (let [width (e/watch !width)
        !start-info (atom nil) start-info (e/watch !start-info)]
    (letfn [(handle-mouse-move [event]
              (let [{:keys [start-x start-width]} start-info
                    current-x (.-clientX event)
                    dx (- current-x start-x)
                    new-width (+ start-width dx)
                    clamped-width (max min-width (min new-width max-width))]
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
       (dom/props {:class "absolute top-0 right-0 bottom-0 w-2 hover:w-0.5 bg-transparent hover:bg-gray-400 cursor-col-resize select-none"})
       (dom/On "mousedown" (fn [e]
                             (reset! !start-info {:start-x (.-clientX e)
                                                  :start-width width})
                             (.preventDefault e) ; Prevent text selection
                             (.addEventListener js/document "mousemove" handle-mouse-move)
                             (.addEventListener js/document "mouseup" handle-mouse-up)) nil)))))