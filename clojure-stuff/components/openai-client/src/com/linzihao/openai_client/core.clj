(ns com.linzihao.openai-client.core
  (:require
   [missionary.core :as m]
   [clj-http.client :as http]
   [cheshire.core :as json]
   [clojure.string :as str]
   [clojure.java.io :as io]
   [aero.core :refer [read-config]])
  (:import [java.io InputStream]))

(def config 
  (read-config (io/resource "openai-client/config.edn")))

(def token (get-in config [:secrets :akash-apikey]))

(def DEFAULT_MODEL "DeepSeek-R1")

(defn- parse-event [raw-event]
  (if (= raw-event "[DONE]")
    {:status :done}
    (json/decode raw-event true)))

(defn stream-chat [model messages]
  (m/ap
   (let [response (m/? (m/via m/blk (http/post "https://chatapi.akash.network/api/v1/chat/completions"
                                               {:form-params {:model model
                                                              :stream true
                                                              :messages messages}
                                                :headers {"Authorization" (str "Bearer " token)}
                                                :content-type :json
                                                :accept :json
                                                :as :stream})))
         event-stream ^InputStream (:body response)]
     (loop [accum ""]
       (let [byte-array (byte-array 4096)
             bytes-read (m/? (m/via m/blk (.read event-stream byte-array)))
             new-data (if (neg? bytes-read)
                        accum
                        (str accum (String. byte-array 0 bytes-read "UTF-8")))
             split (str/split new-data #"\n\n" 2)]
         (if (next split)
           (let [event (parse-event (subs (first split) (count "data: ")))]
             (if (and (map? event) (= (:status event) :done))
               (do (println "Received DONE token, closing stream")
                   (.close event-stream)
                   (m/amb))
               (m/amb event (recur (second split)))))
           (if (neg? bytes-read)
             (do (println "Input stream closed, exiting read-loop")
                 (.close event-stream)
                 (m/amb))
             (recur (first split)))))))))

(def openai-chunk->response
  (fn [x]
    (let [choices (:choices x)
          choice (first choices)
          content (-> choice :delta :content)]
      content)))

(defn openai-chat
  [model messages]
  (let [flow (stream-chat model messages)]
    (m/eduction (map openai-chunk->response) flow)))

(defn debug-streaming-response
  "输入一个 missionary flow, 实时打印每个 token 的内容. 最后返回完整的 response string."
  [flow> & {:keys [chunk->content]
            :or {chunk->content (fn [x] (:content (:message x)))}}]
  (let [sb (StringBuilder.)
        main (m/reduce (fn [_ x]
                         (let [content (chunk->content x)]
                           (.append sb content)
                           (print content)
                           (flush)))
                       nil flow>)]
    (m/? main)
    (.toString sb)))

(comment 
  (time
   (debug-streaming-response
    (openai-chat DEFAULT_MODEL [{:role "user" :content "Hello"}])
    :chunk->content identity))
  :rcf)

