(ns com.linzihao.xiangqi.engine.interface
  (:require
   [com.linzihao.xiangqi.engine.uci :as uci]))

(defn start-engine
  [engine-path]
  (uci/start-engine engine-path))

(defn send-command
  [engine command]
  (uci/send-command engine command))

(defn engine->bestmove-flow
  "Returns a Missionary flow that emits best moves from the engine."
  [engine]
  (uci/engine->bestmove-flow engine))

(defn watch-bestmove
  [engine !bestmove]
  (uci/watch-bestmove engine !bestmove))

(defn stop-engine
  [engine]
  (uci/stop-engine engine))
