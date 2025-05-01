(ns com.linzihao.web.notion.PageOptions
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   #_[com.linzihao.web.svg.icon :refer [Star Link Duplicate Compose ArrowTurnUpRight Trash ArrowSquarePathUpDown ArrowDiagonalUpRight PeekSide]]))

(e/defn MenuItem [icon label shortcut selected?]
  (dom/div
   (dom/props {:tabIndex -1
               :style (merge {:user-select "none" :transition "background 20ms ease-in"
                              :cursor "pointer" :width "100%" :display "flex" :border-radius "6px"}
                             (when selected? {:background "rgba(55, 53, 47, 0.06)"}))})
   (dom/div
    (dom/props {:style {:display "flex" :align-items "center" :gap "8px" :line-height "120%"
                        :width "100%" :user-select "none" :min-height "28px" :font-size "14px"
                        :padding-left "8px" :padding-right "8px"}})
    ;; Icon
    (dom/div
     (dom/props {:style {:display "flex" :align-items "center" :justify-content "center"
                         :min-width "20px" :min-height "20px"}})
     icon)
    ;; Label
    (dom/div
     (dom/props {:style {:margin-left "0px" :margin-right "0px" :min-width "0px" :flex "1 1 auto"}})
     (dom/div
      (dom/props {:style {:white-space "nowrap" :overflow "hidden" :text-overflow "ellipsis"}})
      (dom/text label)))
    ;; Shortcut (optional)
    (when shortcut
      (dom/div
       (dom/props {:style {:margin-left "auto" :min-width "0px" :flex-shrink 0}})
       (dom/span
        (dom/props {:style {:color "rgba(70, 68, 64, 0.45)"}})
        (dom/text shortcut)))))))

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
     #_(MenuItem {:icon (Star) :label "Add to Favorites"}))
    ;; Divider + more items
    #_(dom/div
     (dom/props {:style {:gap "1px" :position "relative" :padding "4px"
                         :display "flex" :flex-direction "column" :margin-top "1px"}})
     (dom/div
      (dom/props {:style {:position "absolute" :top "-1px" :left "12px" :right "12px"
                          :height "1px" :background "rgba(55, 53, 47, 0.09)"}}))
     (menu-item {:icon (Link) :label "Copy link"})
     (menu-item {:icon (Duplicate) :label "Duplicate" :shortcut "Ctrl+D" :selected? true})
     (menu-item {:icon (Compose) :label "Rename" :shortcut "Ctrl+⇧+R"})
     (menu-item {:icon (ArrowTurnUpRight) :label "Move to" :shortcut "Ctrl+⇧+P"})
     (menu-item {:icon (Trash) :label "Move to Trash"}))
    ;; Divider + more items
    #_(dom/div
     (dom/props {:style {:gap "1px" :position "relative" :padding "8px 4px"
                         :display "flex" :flex-direction "column" :margin-top "1px"}})
     (dom/div
      (dom/props {:style {:position "absolute" :top "-1px" :left "12px" :right "12px"
                          :height "1px" :background "rgba(55, 53, 47, 0.09)"}}))
     ;; Last edited info
     (dom/div
      (dom/props {:style {:display "flex" :align-items "center" :gap "8px" :line-height "120%"
                          :width "100%" :user-select "none" :min-height "28px" :font-size "14px"
                          :padding-left "8px" :padding-right "8px"}})
      (dom/div
       (dom/props {:style {:margin-left "0px" :margin-right "0px" :min-width "0px"
                           :flex "1 1 auto"}})
       (dom/div
        (dom/props {:style {:font-size "12px" :line-height "16px" :color "rgba(70, 68, 64, 0.45)"
                            :margin-bottom "4px"}})
        (dom/text "Last edited by 林子豪"))
       (dom/div
        (dom/props {:style {:font-size "12px" :line-height "16px" :color "rgba(70, 68, 64, 0.45)"
                            :white-space "nowrap" :overflow "hidden" :text-overflow "ellipsis"
                            :margin-bottom "0px"}})
        (dom/text "Mar 21, 2023, 8:47 AM"))))))
   ;; Bottom sticky portal
   #_(dom/div
      (dom/props {:class "sticky-portal-target" :data-sticky-stack-name "default"
                  :style {:position "sticky" :z-index 85 :width "100%" :bottom "0px" :left "0px" :flex "0 1 0%"}}))))