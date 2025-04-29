(ns com.linzihao.openai-client.tools-test
  (:require 
   [com.linzihao.openai-client.clients :as clients]
   [com.linzihao.openai-client.chatbot :refer [chatbot]]))

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
     :strict true}}])

(def tool->f
  {"get_weather" (constantly 42)})

(def client
  (-> clients/modelscope-client
      (assoc :tools tools)
      (assoc :tool->f tool->f)))

(comment 
  (def !history (atom [{:role "system" :content "You are a helpful assistant."}]))
  (chatbot !history client "how's the weather in guangzhou?")
  :rcf)

