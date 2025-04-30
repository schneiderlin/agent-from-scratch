(ns com.linzihao.web.generic-components.icon
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]))

(e/defn Icon [extra-classes props Svg]
  (dom/div
   (dom/props (merge {:role "button"
                      :aria-expanded false
                      :class (concat ["select-none transition-opacity duration-200 ease"
                                      "inline-flex items-center justify-center flex-shrink-0 rounded-md h-7 w-7 p-0 relative ml-2 fill-[rgba(55,53,47,0.45)] mr-1"
                                      "hover:bg-gray-200"]
                                     extra-classes)
                      :aria-disabled true}
                     props))
   (Svg)))