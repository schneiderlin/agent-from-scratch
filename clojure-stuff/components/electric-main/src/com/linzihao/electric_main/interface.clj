(ns com.linzihao.electric-main.interface
  (:require [com.linzihao.electric-main.server-jetty :as jetty]))

(defn start-server! [entrypoint
                     {:keys [port host]
                      :or   {port 8080, host "0.0.0.0"}
                      :as   config}]
  (jetty/start-server! entrypoint config))