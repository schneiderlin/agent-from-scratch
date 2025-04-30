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

(e/defn Chevron []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :class "chevronDownRoundedThick" :style {:width "18px" :height "18px" :display "block" :fill "rgba(55,53,47,0.35)"}})
   (svg/path (dom/props {:d "M6.02734 8.80274C6.27148 8.80274 6.47168 8.71484 6.66211 8.51465L10.2803 4.82324C10.4268 4.67676 10.5 4.49609 10.5 4.28125C10.5 3.85156 10.1484 3.5 9.72363 3.5C9.50879 3.5 9.30859 3.58789 9.15234 3.74902L6.03223 6.9668L2.90722 3.74902C2.74609 3.58789 2.55078 3.5 2.33105 3.5C1.90137 3.5 1.55469 3.85156 1.55469 4.28125C1.55469 4.49609 1.62793 4.67676 1.77441 4.82324L5.39258 8.51465C5.58789 8.71973 5.78808 8.80274 6.02734 8.80274Z"}))))

(e/defn Ellipsis []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :class "ellipsisSmall" :style {:width "18px" :height "18px" :display "block" :fill "rgba(71,70,68,0.6)"}})
   (svg/path (dom/props {:d "M3.2 6.725a1.275 1.275 0 1 0 0 2.55 1.275 1.275 0 0 0 0-2.55m4.8 0a1.275 1.275 0 1 0 0 2.55 1.275 1.275 0 0 0 0-2.55m4.8 0a1.275 1.275 0 1 0 0 2.55 1.275 1.275 0 0 0 0-2.55"}))))

(e/defn Plus []
  (svg/svg
   (dom/props {:viewBox "0 0 20 20" :class "plusSmall" :style {:width "18px" :height "18px" :display "block" :fill "rgba(71,70,68,0.6)"}})
   (svg/path (dom/props {:d "M8 2.74a.66.66 0 0 1 .66.66v3.94h3.94a.66.66 0 0 1 0 1.32H8.66v3.94a.66.66 0 0 1-1.32 0V8.66H3.4a.66.66 0 0 1 0-1.32h3.94V3.4A.66.66 0 0 1 8 2.74"}))))
