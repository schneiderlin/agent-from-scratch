(ns com.linzihao.xiangqi.ui.render
  (:require [hyperfiddle.electric3 :as e]
            [hyperfiddle.electric-dom3 :as dom]
            #?(:clj [com.linzihao.xiangqi.interface :as logic])))

(def !selected-pos (atom nil))
#?(:clj (def !debug-pos (atom nil)))
#?(:clj (def !state (atom logic/state)))

(comment
  (reset! !debug-pos [8 4]) 
  :rcf)

(e/defn ChessPiece [{:keys [piece selected? row col next-player]}]
  (e/client
   (dom/div
    (dom/props
     {:class (str "w-24 h-24 flex items-center justify-center rounded-full border-8 select-none "
                  (if selected? "border-primary" "border-base-300")
                  " " (if (= "红" (subs (name piece) 0 1))
                        "text-red-600 bg-base-100"
                        "text-neutral-800 bg-base-100")
                  (if (= (subs (name piece) 0 1) next-player)
                    " cursor-pointer hover:bg-neutral-100"
                    " opacity-50 cursor-not-allowed")
                  " transition-colors")})
    #_(when (= (subs (name piece) 0 1) next-player)
      (dom/on "click" (e/fn [_] (reset! !selected-pos [row col]))))
    (dom/span
     (dom/props {:class "text-5xl font-bold"})
     (dom/text (last (name piece)))))))

(e/defn Chessboard []
  (e/client
   (let [state (e/server (e/watch !state))
         board (e/server (e/watch (atom (:board state))))
         selected-pos (e/watch !selected-pos)
         debug-pos (e/server (e/watch !debug-pos))
         next-player (:next state)
         possible-moves (if selected-pos
                          (e/server (logic/possible-move state selected-pos))
                          [])]
     (dom/div
      (dom/props {:class "relative bg-[url('/image/Xiangqi_board.svg')] bg-contain w-[900px] h-[1200px]"})

      (e/for-by
       identity [row (range 10)]
       (e/for-by
        identity [col (range 9)]
        (let [piece (get-in board [row col])
              top (+ 100 (* row 100))
              left (* col 100)]
          (when piece
            (dom/div
             (dom/props {:class "absolute"
                         :style {:transform (str "translate(" left "px, " top "px)")}})
             (ChessPiece
              {:piece piece
               :selected? (= selected-pos [row col])
               :row row
               :col col
               :next-player next-player}))))))

      ;; Highlight possible moves first (under pieces)
      (e/for-by identity [move possible-moves]
                (let [[row col] move
                      top (+ 100 (* row 100))
                      left (* col 100)]
                  (dom/div
                   (dom/props {:class "absolute w-24 h-24 bg-green-500/20 rounded-full"
                               :style {:transform (str "translate(" left "px, " top "px)")}})
                   #_(dom/on "click"
                           (e/fn [_]
                             (when (and selected-pos
                                        (= (subs (name (get-in board selected-pos)) 0 1) next-player))
                               (e/server (swap! !state #(logic/move % selected-pos [row col])))
                               (reset! !selected-pos nil)))))))

      ;; Add debug position highlight
      (when-let [[row col] debug-pos]
        (let [top (+ 100 (* row 100))
              left (* col 100)]
          (println "debug")
          (dom/div
           (dom/props {:class "absolute w-24 h-24 bg-blue-500/20 rounded-full"
                       :style {:transform (str "translate(" left "px, " top "px)")}}))))))))
