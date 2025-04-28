(ns com.linzihao.openai-client.tools-test
  (:require
   [com.linzihao.openai-client.datalevin-tools :as datalevin-tools]
   [com.linzihao.openai-client.core :as core]
   [clojure.edn :as edn]
   [cheshire.core :as json]))

(def client (update core/deepseek-client :tools concat datalevin-tools/tools))

(comment
  (let [json-args (json/decode "{\"qry\":\"[:find ?e ?name ?nation :where [?e :name ?name] [?e :nation ?nation] [?e :name \\\"De Morgan\\\"]]\",\"args\":\"[]\",\"limit\":1}")
        qry (edn/read-string (get json-args "qry"))
        args (edn/read-string (get json-args "args"))
        limit (or (get json-args "limit") 10)]
    (datalevin-tools/q qry args))

  (time
   (core/reduce-streaming-response
    (core/openai-chat client
                      [{:role "user" :content "find me information about De Morgan in datalevin"}
                       (core/tool-calls->msg {:index 0,
                                              :id "call_0_3effc9f6-5708-4931-bea7-10f367608abb",
                                              :type "function",
                                              :function {:name "schema", :arguments "{}"}})
                       (core/result->tool-resp datalevin-tools/schema "call_0_3effc9f6-5708-4931-bea7-10f367608abb")
                       (core/tool-calls->msg {:index 0,
                                              :id "call_0_e92db31a-18e1-4d08-83ca-c5ae396274e1",
                                              :type "function",
                                              :function
                                              {:name "q",
                                               :arguments
                                               "{\"qry\":\"[:find ?e ?name ?nation :where [?e :name ?name] [?e :nation ?nation] [?e :name \\\"De Morgan\\\"]]\",\"args\":\"[]\",\"limit\":1}"}})
                       (core/result->tool-resp #{[3 "De Morgan" "English"]} "call_0_e92db31a-18e1-4d08-83ca-c5ae396274e1")])
    :chunk->content identity))

  :rcf)

