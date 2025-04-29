(ns com.linzihao.openai-client.chatbot
  (:require
   [cheshire.core :as json]
   [com.linzihao.openai-client.core :as core]))

(defn tool-call->result [client {:keys [function]}]
  (let [{:keys [name arguments]} function
        args (json/decode arguments true)
        f (get (:tool->f client) name)]
    (f args)))

(comment
  (tool-call->result 
   {:tool->f {"get_weather" (constantly 42)}}
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
         {:keys [content reasoning-content tool-calls] :as _resp}
         (core/reduce-streaming-response
          (core/openai-chat client
                            @!history-atom))
         bot-msg (cond-> {:role "assistant"}
                   content (assoc :content content)
                   reasoning-content (assoc :reasoning-content reasoning-content)
                   tool-calls (assoc :tool_calls [tool-calls]))
         _ (println "tool call:" tool-calls)] 
     (if (nil? tool-calls)
       (swap! !history-atom conj bot-msg)
       (let [res-msg (core/result->tool-resp
                      (tool-call->result client tool-calls)
                      (:id tool-calls))]
         (swap! !history-atom conj bot-msg res-msg)
         (chatbot !history-atom client))))))

(comment
  (def !history (atom []))

  (require '[com.linzihao.openai-client.clients :as clients :refer [openrouter-client]])
  (chatbot !history openrouter-client "how's the weather in guangzhou")
  :rcf)

;; memory system test
(comment
  (def system-prompt "You are FiFi, a powerful agentic AI assistant designed by linzihao.
IMPORTANT: If you state that you will use a tool, immediately call that tool as your next action.
Before calling each tool, first explain why you are calling it.
<memory_system> You have access to a persistent memory database to record important context about the USER's task, 
codebase, requests, and preferences for future reference. As soon as you encounter important information or context, proactively use the add-note or update-note tool to save it to the database. You DO NOT need to wait until the end of a task to create a memory or a break in the conversation to create a memory. 
You DO NOT need to be conservative about creating memories. Any memories you create will be presented to the USER, who can reject them if they are not aligned with their preferences. 
Remember that you have a limited context window and ALL CONVERSATION CONTEXT, will be deleted. 
Therefore, you should create memories liberally to preserve key context. To retrieve information: You must actively search for relevant memories using:
search-note (to find notes by keyword) → Returns a list of filenames.
read-note (to read a specific note's content).
IMPORTANT: ALWAYS pay attention to memories, as they provide valuable context to guide your behavior and solve the task. </memory_system>")
  
  (def !history (atom [{:role "system" :content system-prompt}]))

  (chatbot !history openrouter-client "") 
  :rcf)

