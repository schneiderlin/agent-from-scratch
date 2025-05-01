(ns com.linzihao.web.generic-components.rich-label
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   [com.linzihao.web.hooks.hook :refer [Hoverable]]))

;; 左边一个图标, 中间 label, 右边任意内容的布局组件

(e/defn RichLabel
  "All electric function in this component will call with hover? parameter"
  ([LeftContent label RightContent] (RichLabel LeftContent label RightContent nil))
  ([LeftContent label RightContent Siblings]
   (let [!hover? (atom false) hover? (e/watch !hover?)]
     (dom/div
      (dom/props {:class "flex items-center gap-2 py-1 rounded-md cursor-pointer hover:bg-gray-100 justify-start text-left select-none"})
      (Hoverable !hover?)
      (LeftContent hover?)
      ;; Label text
      (dom/span
       (dom/props {:class "flex-1 overflow-hidden whitespace-nowrap text-ellipsis"})
       (dom/text label))
      ;; Right action icons
      (RightContent hover?))
     (when Siblings
       (Siblings hover?)))))