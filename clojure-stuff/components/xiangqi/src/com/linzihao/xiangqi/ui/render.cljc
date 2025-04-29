(ns com.linzihao.xiangqi.ui.render
  (:require
   [missionary.core :as m]
   [hyperfiddle.electric3 :as e]
   [hyperfiddle.electric-dom3 :as dom]
   #?(:clj [com.linzihao.xiangqi.interface :as logic])
   #?(:clj [com.linzihao.xiangqi.engine.interface :as ei])
   #?(:clj [com.linzihao.xiangqi.fen :as fen])))

(defonce !selected-pos (atom nil))
#?(:clj (def !debug-pos (atom nil)))
#?(:clj (defonce !state (atom logic/state)))
#?(:clj (defonce engine (ei/start-engine "/home/linzihao/Desktop/workspace/private/agent-from-scratch/data/pikafish/pikafish-avx2")))
#?(:clj (defonce !bestmove (atom nil)))
;; #?(:clj (defonce bestmove-flow (m/watch !bestmove)))
#?(:clj (defonce bestmove-flow (ei/engine->bestmove-flow engine)))


(comment 
  (reset! !bestmove "h2e2")
  (def bestmove-flow (m/watch !bestmove))
  (def bestmove-flow (ei/engine->bestmove-flow engine))
  (def engine (ei/start-engine "/home/linzihao/Desktop/workspace/private/agent-from-scratch/data/pikafish/pikafish-avx2"))

  engine
  (ei/send-command engine "go depth 10")
  (ei/send-command engine "uci")
  (ei/send-command engine "isready")
  (ei/send-command engine "d")
  (ei/send-command engine "stop")
 

  (reset! !debug-pos [8 4])
  (require '[com.linzihao.xiangqi.fen :refer [fen->state move-str->coords]])
  (reset! !state (fen->state
                  "9/9/3k5/9/9/9/4R4/3A5/8r/4K4 b - - 0 1"))

  (move-str->coords "i1i0")
  (reset! !state logic/state)
  :rcf)

(e/defn ChessPiece [{:keys [piece selected? row col next-player]}]
  (e/client
   (dom/div
    (dom/props
     {:class (str "w-24 h-24 flex items-center justify-center rounded-full border-8 select-none "
                  (if selected? "border-primary" "border-base-300")
                  " " (if (= "红" (subs (name piece) 0 1))
                        "text-red-600 bg-white"
                        "text-neutral-800 bg-white")
                  (if (= (subs (name piece) 0 1) next-player)
                    " cursor-pointer hover:bg-neutral-100"
                    " cursor-not-allowed")
                  " transition-colors")})
    (when (= (subs (name piece) 0 1) next-player)
      (e/for [[_token _event] (dom/On-all "click")]
        (reset! !selected-pos [row col])))
    (dom/span
     (dom/props {:class "text-5xl font-bold"})
     (dom/text (last (name piece)))))))

(e/defn Go [state]
  (e/server
   (let [new-fen (fen/state->fen state)]
     (println "start thinking in new pos")
     (ei/send-command engine (str "position fen " new-fen))
     (ei/send-command engine "go depth 10")
     nil)))

(e/defn Chessboard []
  (e/client
   (let [state (e/server (e/watch !state))
         board (e/server (e/watch (atom (:board state))))
         bestmove (e/server (fen/move-str->coords (e/input bestmove-flow)))
         selected-pos (e/watch !selected-pos)
         debug-pos (e/server (e/watch !debug-pos))
         next-player (:next state)
         possible-moves (if selected-pos
                          (e/server (logic/possible-move state selected-pos))
                          [])]
     (Go state)
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
                   (e/for [[_token _event] (dom/On-all "click")]
                     (when (and selected-pos
                                (= (subs (name (get-in board selected-pos)) 0 1) next-player))
                       (e/server
                        (swap! !state #(logic/move % selected-pos [row col])))
                       (reset! !selected-pos nil))))))

      ;; Highlight bestmove-coords (red highlight)
      (when bestmove
        (let [[[from-row from-col] [to-row to-col]] bestmove
              from-top (+ 100 (* from-row 100))
              from-left (* from-col 100)
              to-top (+ 100 (* to-row 100))
              to-left (* to-col 100)]
          ;; Optionally print for debug
          #_(println from-row from-col to-row to-col)
          ;; From-square highlight (optional, uncomment if both are desired)
          (dom/div
           (dom/props {:class "absolute w-24 h-24 bg-red-500/30 rounded-full border-4 border-red-600 animate-pulse pointer-events-none"
                       :style {:transform (str "translate(" from-left "px, " from-top "px)")}}))
          ;; To-square highlight (main highlight)
          (dom/div
           (dom/props {:class "absolute w-24 h-24 bg-red-500/30 rounded-full border-4 border-red-600 animate-pulse pointer-events-none"
                       :style {:transform (str "translate(" to-left "px, " to-top "px)")}}))))

      ;; Add debug position highlight
      (when-let [[row col] debug-pos]
        (let [top (+ 100 (* row 100))
              left (* col 100)]
          (println "debug")
          (dom/div
           (dom/props {:class "absolute w-24 h-24 bg-blue-500/20 rounded-full"
                       :style {:transform (str "translate(" left "px, " top "px)")}}))))))))
