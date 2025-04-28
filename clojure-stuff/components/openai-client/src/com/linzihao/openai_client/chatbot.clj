(ns com.linzihao.openai-client.chatbot
  (:require
   [cheshire.core :as json]
   [com.linzihao.openai-client.core :as core]))

(defn get-weather [{:keys [latitude longitude]}]
  (str latitude "," longitude ": " 23 " degrees"))

(defn add-memory [{:keys [memory_text]}]
  (str "added memory: " memory_text))

(def tool->f
  {"get_weather" get-weather
   "add_memory" add-memory})

(def client (assoc core/deepseek-client :tools core/tools))

(defn tool-call->result [{:keys [function]}]
  (let [{:keys [name arguments]} function
        args (json/decode arguments true)
        f (get tool->f name)]
    (f args)))

(comment
  (tool-call->result 
   {:function {:name "get_weather" 
               :arguments "{\"latitude\":23.1291,\"longitude\":113.2644}"}})
  :rcf)

(defn chatbot
  "prompt can be a string, which is equvelent to {:role \"user\" :content prompt}
   or can be a map with :role and :content key"
  ([!history-atom client]
   (chatbot !history-atom client nil))
  ([!history-atom client prompt]
   (let [msg (cond
               (nil? prompt) nil
               (string? prompt) {:role "user" :content prompt}
               :else prompt)
         _ (when msg (swap! !history-atom conj msg))
         resp (core/reduce-streaming-response
               (core/openai-chat client
                                 @!history-atom))]
     (if (string? resp)
       (swap! !history-atom conj {:role "assistant" :content resp})
       (let [tool-call resp
             _ (println "tool call:" tool-call)
             rep-msg (core/tool-calls->msg tool-call)
             res-msg (core/result->tool-resp
                      (tool-call->result tool-call)
                      (:id tool-call))]
         (swap! !history-atom conj rep-msg res-msg)
         (chatbot !history-atom client))))))

(comment
  (def !history (atom []))
  (chatbot !history client "how's the weather in guangzhou")
  :rcf)

