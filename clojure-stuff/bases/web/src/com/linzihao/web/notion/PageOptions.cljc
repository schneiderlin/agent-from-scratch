(ns com.linzihao.web.notion.PageOptions
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   [com.linzihao.web.generic-components.rich-label :refer [RichLabel]]
   [com.linzihao.web.svg.icon :refer [Document Star Duplicate Compose ArrowDiagonalUpRight
                                      Trash]]))

(e/defn MenuItem [Icon label shortcut]
  (RichLabel
   (e/fn [_] (Icon))
   label
   (e/fn [_]
     (dom/span
      (dom/props {:class "text-[12px] text-neutral-800 whitespace-nowrap"})
      (dom/text shortcut)))))

(e/defn PageOptions []
  (dom/div
   ;; stick to icon's top left
   (dom/props {:class "absolute left-0 top-4 z-50 bg-white rounded-xl shadow-xl border border-gray-200"
               :style {:min-width "220px"}})
   ;; Menu container
   (dom/div
    (dom/props {:tabIndex 0 :role "menu"
                :style {:border-radius "10px"}})
    ;; Main menu group
    (dom/div
     (dom/props {:style {:gap "1px" :position "relative" :padding "4px"
                         :display "flex" :flex-direction "column"}})
     ;; Menu item: Add to Favorites
     (MenuItem Document "Add to Favorites" nil))
    ;; Divider + more items
    (dom/div
     (dom/props {:style {:gap "1px" :position "relative" :padding "4px"
                         :display "flex" :flex-direction "column" :margin-top "1px"}})
     (dom/div
      (dom/props {:style {:position "absolute" :top "-1px" :left "12px" :right "12px"
                          :height "1px" :background "rgba(55, 53, 47, 0.09)"}}))
     (MenuItem Star "Copy link" nil)
     (MenuItem Duplicate "Duplicate" "Ctrl+D")
     (MenuItem Compose "Rename" "Ctrl+⇧+R")
     (MenuItem ArrowDiagonalUpRight "Move to" "Ctrl+⇧+P")
     (MenuItem Trash "Move to Trash" nil)))
   ;; last modified info
   (dom/div
    (dom/props {:class "flex flex-col gap-0 w-full select-none min-h-[28px] text-[14px] px-2 py-2"})
    (dom/div
     (dom/props {:class "text-[12px] leading-4 text-[rgba(70,68,64,0.45)] mb-1"})
     (dom/text "Last edited by 林子豪"))
    (dom/div
     (dom/props {:class "text-[12px] leading-4 text-[rgba(70,68,64,0.45)] whitespace-nowrap overflow-hidden text-ellipsis mb-0"})
     (dom/text "Mar 21, 2023, 8:47 AM")))))