(ns com.linzihao.web.generic-components.label-button
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom] 
            [com.linzihao.web.generic-components.icon :refer [Icon]]))

(e/defn LabelButton [Svg label]
  (dom/li
   (dom/props {:class "flex items-center gap-2 py-1 rounded-md cursor-pointer hover:bg-gray-100 justify-start text-left"})
   (Icon [] {}
         Svg)
   (dom/span (dom/text label))))