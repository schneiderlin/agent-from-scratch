(ns com.linzihao.web.svg.icon
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   [hyperfiddle.electric-svg3 :as svg]))

(e/defn Compose []
  (svg/svg
   (dom/props {:aria-hidden "true"
               :role "graphics-symbol"
               :viewBox "0 0 20 20"
               :class "compose"
               :style {:width "22px" :height "22px" :display "block" :fill "#322C2C" :flex-shrink 0}})
   (svg/path (dom/props {:d "m16.774 4.341-.59.589-1.109-1.11.596-.594a.784.784 0 0 1 1.103 0c.302.302.302.8 0 1.102zM8.65 12.462l6.816-6.813-1.11-1.11-6.822 6.808a1.1 1.1 0 0 0-.236.393l-.289.932c-.052.196.131.38.315.314l.932-.288a.9.9 0 0 0 .394-.236"}))
   (svg/path (dom/props {:d "M4.375 6.25c0-1.036.84-1.875 1.875-1.875H11a.625.625 0 1 0 0-1.25H6.25A3.125 3.125 0 0 0 3.125 6.25v7.5c0 1.726 1.4 3.125 3.125 3.125h7.5c1.726 0 3.125-1.4 3.125-3.125V9a.625.625 0 1 0-1.25 0v4.75c0 1.036-.84 1.875-1.875 1.875h-7.5a1.875 1.875 0 0 1-1.875-1.875z"}))))

(e/defn Search []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M8.5 3.5a5 5 0 1 1 0 10 5 5 0 0 1 0-10zm7 13-3.5-3.5" :strokeWidth "1.5" :strokeLinecap "round" :strokeLinejoin "round"}))))

(e/defn Home []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M3 9.5 10 4l7 5.5V16a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z" :strokeWidth "1.5" :strokeLinejoin "round"}))))

(e/defn Inbox []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M3.5 6.5v7a2 2 0 0 0 2 2h9a2 2 0 0 0 2-2v-7a2 2 0 0 0-2-2h-9a2 2 0 0 0-2 2zm0 7 3.5-3 2.5 2 2.5-2 3.5 3" :strokeWidth "1.5" :strokeLinejoin "round"}))))

(e/defn Document []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :fill "none" :stroke "currentColor" :style {:width "18px" :height "18px"}})
   (svg/path (dom/props {:d "M6 2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V7.828A2 2 0 0 0 15.414 7L13 4.586A2 2 0 0 0 11.586 4H6zm0 0h5a1 1 0 0 1 1 1v3a1 1 0 0 0 1 1h3M6 2v16m8-8H6" :strokeWidth "1.5" :strokeLinejoin "round" :strokeLinecap "round"}))))