(ns com.linzihao.web.generic-components.icon
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn IconButton [extra-classes props on-click Svg]
  (dom/div
   (dom/props (merge {:role "button"
                      :aria-expanded false
                      :class (concat ["select-none transition-opacity duration-200 ease"
                                      "inline-flex items-center justify-center flex-shrink-0 rounded-md p-0 relative fill-[rgba(55,53,47,0.45)] w-[20px] h-[20px]"
                                      "hover:bg-gray-200"]
                                     extra-classes)
                      :aria-disabled true}
                     props))
   (dom/On "click" on-click nil)
   (Svg)))

(e/defn Icon [extra-classes props Svg]
  (dom/div
   (dom/props (merge {:role "button"
                      :aria-expanded false
                      :class (concat ["select-none transition-opacity duration-200 ease"
                                      "inline-flex items-center justify-center flex-shrink-0 rounded-md p-0 relative fill-[rgba(55,53,47,0.45)]"]
                                     extra-classes)
                      :aria-disabled true}
                     props))
   (Svg)))