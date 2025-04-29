(ns com.linzihao.xiangqi.fen
  (:require [clojure.string :as str]))

(def piece-map
  {"k" :黑将, "a" :黑士, "b" :黑象, "n" :黑马, "r" :黑车, "c" :黑炮, "p" :黑卒
   "K" :红帅, "A" :红士, "B" :红相, "N" :红马, "R" :红车, "C" :红炮, "P" :红兵})

(defn fen-row->board-row [row]
  (loop [chars (seq row) acc []]
    (if (empty? chars)
      acc
      (let [c (first chars)]
        (if (Character/isDigit c)
          (recur (rest chars) (into acc (repeat (- (int c) (int \0)) nil)))
          (recur (rest chars) (conj acc (piece-map (str c)))))))))

(defn fen->state [fen]
  (let [[board-part turn & _] (str/split fen #" ")
        rows (str/split board-part #"/")
        board (vec (map fen-row->board-row rows))
        next (case turn
               "b" "黑"
               "w" "红")]
    {:board board
     :next next
     :prev-move nil}))

(comment
  (fen->state "9/9/3k5/9/9/9/4R4/3A5/8r/4K4 b - - 0 1")
  :rcf)
