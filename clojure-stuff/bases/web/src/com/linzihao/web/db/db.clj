(ns com.linzihao.web.db.db
  (:require 
   [datalevin.core :as d]
   [datalevin.search-utils :as search-utils]))

(defn get-blocks-in-page
  "Given a page-id, return a vector of blocks inside it, each as {:id id :type type :data data}."
  [conn page-id]
  (println "page-id" page-id)
  (let [data (d/q '[:find ?data .
                    :in $ ?pid
                    :where
                    [?e :block/id ?pid]
                    [?e :block/type "page"]
                    [?e :block/data ?data]]
                  (d/db conn) page-id)
        block-ids (:blocks data)]
    (vec
     (for [bid block-ids]
       (let [[id type data] (first (d/q '[:find ?bid ?type ?data
                                          :in $ ?bid
                                          :where
                                          [?b :block/id ?bid]
                                          [?b :block/type ?type]
                                          [?b :block/data ?data]]
                                        (d/db conn) bid))]
         {:id id :type type :data data})))))

(comment 
  (get-blocks-in-page conn "home")
  (get-blocks-in-page conn "Page 1")
  :rcf)

(def schema {:block/id {:db/cardinality :db.cardinality/one
                        :db/valueType :db.type/string
                        :db/unique :db.unique/identity}
             :block/type {:db/cardinality :db.cardinality/one
                          :db/valueType :db.type/string}
             :block/data {:db/cardinality :db.cardinality/one 
                          :db.fulltext/autoDomain true
                          :db/fulltext true}})

(def analyzer (search-utils/create-analyzer
               {:tokenizer (search-utils/create-regexp-tokenizer
                            #"[\s:/\.;,!=?\"'()\[\]{}|<>&@#^*\\~`]+")
                :token-filters [(search-utils/create-ngram-token-filter 2 4)]}))

(comment
  (analyzer "hello world")
  (analyzer "你好世界哈哈哈") 
  :rcf)

(def conn (d/get-conn "./tmp/mydb7" schema
                      {:search-opts {:query-analyzer analyzer
                                     :analyzer analyzer}}))

(defn upsert!
  "Insert or update a block/page entity. Takes conn and a map with :block/id, :block/type, :block/data."
  [conn entity]
  (d/transact! conn [entity]))

(defn page-upsert!
  [conn page-id blocks]
  (let [block-ids (map :block/id blocks)
        page-entity {:block/id page-id 
                     :block/type "page" 
                     :block/data {:blocks (vec block-ids)}}]
   #_(conj blocks page-entity)
    (d/transact! conn (conj blocks page-entity))))

(comment 
  (let [blocks [{:block/id "I5EoiCZbFZ", :block/type "paragraph", :block/data {:text "what the fuck"}}]]
    (page-upsert! conn "home" blocks))

  (upsert! conn 
           {:block/id "XaHGLadjhT"
            :block/type "paragraph"
            :block/data {:text "dhcdhdkwajsdh World"}})

  ;; 全部的 page
  (d/q '[:find ?id ?data
         :where
         [?e :block/id ?id]
         [?e :block/type "page"]
         [?e :block/data ?data]]
       (d/db conn))

  ;; 全部的 block
  (d/q '[:find ?id ?type ?data
         :where
         [?e :block/id ?id]
         [?e :block/type ?type]
         [?e :block/data ?data]]
       (d/db conn))

  ;; page 也是一种 block
  (d/q '[:find ?id ?data
         :where
         [?e :block/id ?id]
         [?e :block/type "page"]
         [?e :block/data ?data]]
       (d/db conn))

  ;; full text search
  (d/q '[:find ?e ?data
         :in $ ?q
         :where 
         [?e :block/data ?data]
         [(fulltext $ ?q) [[?e _ _]]]]
       (d/db conn) 
       "going")

  :rcf)