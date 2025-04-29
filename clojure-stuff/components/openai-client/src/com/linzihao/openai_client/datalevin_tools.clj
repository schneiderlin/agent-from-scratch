(ns com.linzihao.openai-client.datalevin-tools
  (:require [datalevin.core :as d]))

(def schema {:aka  {:db/cardinality :db.cardinality/many}
             :name {:db/valueType :db.type/string
                    :db/unique    :db.unique/identity
                    :db/cardinality :db.cardinality/one}
             :nation {:db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one}})

(def conn (d/get-conn "./tmp/mydb" schema))

(comment
  (d/transact! conn
               [{:name "Frege", :db/id -1, :nation "France", :aka ["foo" "fred"]}
                {:name "Peirce", :db/id -2, :nation "france"}
                {:name "De Morgan", :db/id -3, :nation "English"}])
  :rcf)

(defn q [qry args]
  (apply d/q qry (d/db conn) args))

(def tools
  [{:type "function"
    :function
    {:name "schema"
     :description "Queries Datalevin Schema and returns a seq of EDN maps."
     :parameters
     {:type "object"
      :properties {}
      :required []
      :additionalProperties false}
     :strict true}}
   
   {:type "function"
    :function
    {:name "q"
     :description "Query Datalevin.
      Runs `(->> (apply d/q qry (d/db conn) args) (drop offset) (take limit))`.
      Takes qry, vector of args and limit. Returns a collection of results.

      Use the `schema` tool to learn valid attributes before attempting queries."
     :parameters
     {:type "object"
      :properties {:qry {:type "string"
                         :description "EDN-encoded Datalog Query, e.g. \"[:find ?e :where [?e :some/attr ?value]]\""}
                   :args {:type "string"
                          :description "EDN-encoded vector of Datalog Query Arguments, e.g. \"[1 2 3]\""}
                   :offset {:type "number"
                            :description "Query offset: (->> qry-result (drop offset)). Defaults to 0."}
                   :limit {:type "number"
                           :description "Query limit: (->> qry-result (drop offset) (take limit)). Defaults to 100."}}
      :required ["qry args"]
      :additionalProperties false}
     :strict true}}])

(def tool->f
  {"schema" schema
   "q" q})