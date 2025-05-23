(ns com.linzihao.openai-client.core
  (:require
   [missionary.core :as m]
   [clj-http.client :as http]
   [cheshire.core :as json]
   [clojure.string :as str] 
   [com.rpl.specter :as sp]
   [clojure.pprint :as pprint])
  (:import [java.io InputStream]))

(defn- parse-event [raw-event] 
  (if (= raw-event "[DONE]")
    {:status :done}
    (json/decode raw-event true)))

(comment
  (parse-event "{\"id\":\"e74deba9-0520-4f45-805d-61b3aefe63c8\",\"object\":\"chat.completion.chunk\",\"created\":1745735232,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_8802369eaa_prod0425fp8\",\"choices\":[{\"index\":0,\"delta\":{\"tool_calls\":[{\"index\":0,\"function\":{\"arguments\":\"4\"}}]},\"logprobs\":null,\"finish_reason\":null}]}")
  :rcf)

(defn stream-chat [{:keys [base-url api-key model tools] :as _client} messages]
  (m/ap
   (let [response (m/? (m/via m/blk (try
                                      (http/post (str base-url "/chat/completions")
                                                 {:form-params {:model model
                                                                :tools tools
                                                                :stream true
                                                                :messages messages}
                                                  :headers {"Authorization" (str "Bearer " api-key)}
                                                  :content-type :json
                                                  :accept :json
                                                  :as :stream})
                                      (catch Exception e
                                        (println e)))))
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
               (do (.close event-stream)
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
          reasoning-content (-> choice :delta :reasoning_content)
          content (-> choice :delta :content)
          tool-calls (-> choice :delta :tool_calls)]
      {:content (if (empty? content) nil content)
       :reasoning-content (if (empty? reasoning-content) nil reasoning-content)
       :tool-calls tool-calls})))

(defn openai-chat
  [client messages]
  (let [flow (stream-chat client messages)]
    (m/eduction (map openai-chunk->response) flow)))

(defn combine-tool-call [acc chunk]
  (let [chunk (first chunk)]
    (if-let [args (get-in chunk [:function :arguments])] 
      (sp/transform [:function :arguments]
                    #(str (or % "") args)
                    acc)
      acc)))

(comment
  (combine-tool-call
   {:function {:arguments {:name "get_weather"}, :id "call_d6d3e200cbd24e21b90c19", :index 0, :type "function"}}
   [{:function {:arguments "latitude"}, :id "call_d6d3e200cbd24e21b90c19", :index 0, :type "function"}])
  :rcf)

(defn reduce-streaming-response
  "输入一个 missionary flow, 实时打印每个 token 的内容. 最后返回完整的 response string."
  [flow> & _opts]
  (let [content-sb (StringBuilder.)
        thinking-sb (StringBuilder.)
        main (m/reduce
              (fn [tool-calls-acc {:keys [content tool-calls reasoning-content]}] 
                (when content
                  (.append content-sb content)
                  (print content)
                  (flush))
                (when reasoning-content
                  (.append thinking-sb reasoning-content)
                  (print reasoning-content)
                  (flush))
                (if tool-calls
                  (let [chunk tool-calls]
                    (if (not (nil? tool-calls-acc))
                      (combine-tool-call tool-calls-acc chunk)
                      (first chunk)))
                  tool-calls-acc))
              nil flow>)
        tool-calls-ret (m/? main)
        content-ret (.toString content-sb)
        reasoning-content-ret (.toString thinking-sb)]
    {:content (if (empty? content-ret) nil content-ret)
     :reasoning-content (if (empty? reasoning-content-ret) nil reasoning-content-ret)
     :tool-calls tool-calls-ret}))

(defn result->tool-resp [result tool-call-id]
  {:role "tool"
   :tool_call_id tool-call-id
   :content (with-out-str (pprint/pprint result))})

(comment
  ;; 普通 function call
  (require '[com.linzihao.openai-client.clients :refer [deepseek-client]])

  (time
   (reduce-streaming-response
    (openai-chat deepseek-client [{:role "user" :content "how's the weather in guangzhou?"}
                                  {:role "assistant" :content ""
                                   :tool_calls [{:index 0,
                                                 :id "call_0_5fc90e69-57c3-4a68-8a23-41f2ab0cf4ff",
                                                 :type "function",
                                                 :function {:name "get_weather", :arguments "{\"latitude\":23.1291,\"longitude\":113.2644}"}}]}
                                  {:role "tool"
                                   :tool_call_id "call_0_5fc90e69-57c3-4a68-8a23-41f2ab0cf4ff"
                                   :content "24C"}])
    :chunk->content identity))

  ;; 普通chat
  (time
   (reduce-streaming-response
    (openai-chat deepseek-client [{:role "user" :content "hello"}])
    :chunk->content identity))

  ;; 记忆
  (time
   (reduce-streaming-response
    (openai-chat deepseek-client [{:role "user" :content "hello, my name is linzihao"}])
    :chunk->content identity))
  :rcf)

