(ns com.linzihao.openai-client.core
  (:require
   [missionary.core :as m]
   [clj-http.client :as http]
   [cheshire.core :as json]
   [clojure.string :as str]
   [clojure.java.io :as io]
   [com.rpl.specter :as sp]
   [aero.core :refer [read-config]])
  (:import [java.io InputStream]))

(def config 
  (read-config (io/resource "openai-client/config.edn")))

(def DEFAULT_MODEL "DeepSeek-R1")

(defn- parse-event [raw-event] 
  (if (= raw-event "[DONE]")
    {:status :done}
    (json/decode raw-event true)))

(comment
  (parse-event "{\"id\":\"e74deba9-0520-4f45-805d-61b3aefe63c8\",\"object\":\"chat.completion.chunk\",\"created\":1745735232,\"model\":\"deepseek-chat\",\"system_fingerprint\":\"fp_8802369eaa_prod0425fp8\",\"choices\":[{\"index\":0,\"delta\":{\"tool_calls\":[{\"index\":0,\"function\":{\"arguments\":\"4\"}}]},\"logprobs\":null,\"finish_reason\":null}]}")
  :rcf)

(def tools
  [{:type "function"
    :function
    {:name "get_weather"
     :description "Get current temperature for provided coordinates in celsius."
     :parameters
     {:type "object"
      :properties {:latitude {:type "number"}
                   :longitude {:type "number"}}
      :required ["latitude" "longitude"]
      :additionalProperties false}
     :strict true}}
   {:type "function"
    :function
    {:name "add_memory"
     :description "when user tell you something factual or explicit ask you to remember something, use this function. the memory text should be concise."
     :parameters
     {:type "object"
      :properties {:memory_text {:type "string"}}
      :required ["memory_text"]
      :additionalProperties false}
     :strict true}}])

(def akash-client {:base-url "https://chatapi.akash.network/api/v1"
                   :api-key (get-in config [:secrets :akash-apikey])
                   :model DEFAULT_MODEL
                   :tools tools})

(def deepseek-client {:base-url "https://api.deepseek.com"
                      :api-key (get-in config [:secrets :deepseek-apikey])
                      :model "deepseek-chat"
                      :tools tools})

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
          content (-> choice :delta :content)
          tool-calls (-> choice :delta :tool_calls)]
      (if content
        {:content content}
        {:tool-calls tool-calls}))))

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
      (merge acc chunk))))

(comment
  (def tool-calls-chunks [[{:index 0, :id "call_0_29cff275-a3c7-4d18-808f-68af17e74114", :type "function", :function {:name "get_weather", :arguments ""}}]
                          [{:index 0, :function {:arguments "{\""}}]
                          [{:index 0, :function {:arguments "latitude"}}]
                          [{:index 0, :function {:arguments "\":"}}]
                          [{:index 0, :function {:arguments "23"}}]
                          [{:index 0, :function {:arguments "."}}]
                          [{:index 0, :function {:arguments "129"}}]
                          [{:index 0, :function {:arguments "1"}}]
                          [{:index 0, :function {:arguments ",\""}}]
                          [{:index 0, :function {:arguments "long"}}]
                          [{:index 0, :function {:arguments "itude"}}]
                          [{:index 0, :function {:arguments "\":"}}]
                          [{:index 0, :function {:arguments "113"}}]
                          [{:index 0, :function {:arguments "."}}]
                          [{:index 0, :function {:arguments "264"}}]
                          [{:index 0, :function {:arguments "4"}}]
                          [{:index 0, :function {:arguments "}"}}]])

  (reduce
   combine-tool-call
   (first (first tool-calls-chunks))
   (rest tool-calls-chunks))
  :rcf)

(defn debug-streaming-response
  "输入一个 missionary flow, 实时打印每个 token 的内容. 最后返回完整的 response string."
  [flow> & {:keys [chunk->content]
            :or {chunk->content (fn [x] (:content (:message x)))}}]
  (let [sb (StringBuilder.)
        main (m/reduce
              (fn [tool-calls x] 
                (if-let [content (:content x)]
                  (let [content (chunk->content content)]
                    (.append sb content)
                    (print content)
                    (flush)
                    tool-calls)
                  (let [chunk (:tool-calls x)] 
                    (if (not (nil? tool-calls))
                      (combine-tool-call tool-calls chunk)
                      (first chunk)))))
              nil flow>)
        tool-calls-ret (m/? main) 
        content-ret (.toString sb)]
    (if (empty? content-ret)
      tool-calls-ret
      content-ret)))

(comment
  ;; 普通 function call
  (time
   (debug-streaming-response
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
   (debug-streaming-response
    (openai-chat deepseek-client [{:role "user" :content "hello"}])
    :chunk->content identity))

  ;; 记忆
  (time
   (debug-streaming-response
    (openai-chat deepseek-client [{:role "user" :content "hello, my name is linzihao"}])
    :chunk->content identity))
  :rcf)

