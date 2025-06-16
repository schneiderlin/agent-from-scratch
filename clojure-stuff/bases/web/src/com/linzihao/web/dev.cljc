(ns com.linzihao.web.dev
  (:require
   clojure.edn
   com.linzihao.web.main
   [hyperfiddle.electric3 :as e]
   #?(:cljs hyperfiddle.electric-client3)
   #?(:clj [com.linzihao.electric-main.interface :as jetty])
   #?(:clj [shadow.cljs.devtools.api :as shadow])
   #?(:clj [shadow.cljs.devtools.server :as shadow-server])
   #?(:clj [clojure.tools.logging :as log])))

(comment
  (-main)
  :rcf)

#?(:clj
   (do
     (def config
       (merge
        {:host "0.0.0.0"
         :port 8080
         :resources-path "public/web"
         :manifest-path "public/web/js/manifest.edn"}))

     (defn -main [& args]
       (log/info "Starting Electric compiler and server...")

       (shadow-server/start!) ; no-op in calva shadow-cljs configuration which starts this out of band
       (shadow/watch :web) 
       (comment (shadow-server/stop!))

       (def server (jetty/start-server!
                    (fn [ring-request]
                      (e/boot-server {} com.linzihao.web.main/Main (e/server ring-request)))
                    config))
       (comment
         (.stop server) ; jetty
         (server)       ; httpkit
         ))))

#?(:cljs ; client entrypoint
   (do
     (defonce reactor nil)

     (defn ^:dev/after-load ^:export start! []
       (set! reactor ((e/boot-client {} com.linzihao.web.main/Main (e/server (e/amb))) ; symmetric with e/boot-server: same arity - no-value hole in place of server-only ring-request
                      #(js/console.log "Reactor success:" %)
                      #(js/console.error "Reactor failure:" %))))

     (defn ^:dev/before-load stop! []
       (when reactor (reactor)) ; stop the reactor
       (set! reactor nil))))
