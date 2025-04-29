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

(def file->col
  {\a 0 \b 1 \c 2 \d 3 \e 4 \f 5 \g 6 \h 7 \i 8})

(defn move-str->coords [move-str]
  (let [[from to] [(subs move-str 0 2) (subs move-str 2 4)]
        [from-file from-rank] (seq from)
        [to-file to-rank] (seq to)
        rank->row #(case %
                     \0 9
                     \1 8
                     \2 7
                     \3 6
                     \4 5
                     \5 4
                     \6 3
                     \7 2
                     \8 1
                     \9 0)]
    [[(rank->row from-rank) (file->col from-file)]
     [(rank->row to-rank) (file->col to-file)]]))

(comment
  (fen->state "9/9/3k5/9/9/9/4R4/3A5/8r/4K4 b - - 0 1")
  (move-str->coords "i1i0")
  (move-str->coords "e0e1") 
  :rcf)

