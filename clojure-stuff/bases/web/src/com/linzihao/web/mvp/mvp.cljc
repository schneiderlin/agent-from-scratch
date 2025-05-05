(ns com.linzihao.web.mvp.mvp
  (:require
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]))

(e/defn Children []
  (dom/ul
   (e/for [i (e/diff-by identity (range 100))]
     (dom/div
      (dom/span (dom/text i))))))

(e/defn Menu []
  (let [!expand? (atom false) expand? (e/watch !expand?)]
    (dom/div
     (dom/button
      (dom/On "click" #(swap! !expand? not) nil)
      (dom/text (if expand? "collapse" "expand")))
     (when expand?
       (dom/div
        (dom/props {:class "ml-2"})
        (Children))))))