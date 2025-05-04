(ns com.linzihao.web.db.db
  (:require [datalevin.core :as d]
            [datalevin.analyzer :as analyzer]
            [datalevin.search-utils :as search-utils]
            ))

(defn get-blocks-in-page
  "Given a page-id, return a vector of blocks inside it, each as {:id id :type type :data data}."
  [conn page-id]
  (let [block-ids (d/q '[:find ?blocks .
                         :in $ ?pid
                         :where
                         [?e :block/id ?pid]
                         [?e :block/type "page"]
                         [?e :block/data ?data]
                         [(get ?data :blocks) ?blocks]]
                       (d/db conn) page-id)]
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
  (get-blocks-in-page conn "some page")
  :rcf)

(def schema {:block/id {:db/cardinality :db.cardinality/one
                        :db/valueType :db.type/string}
             :block/type {:db/cardinality :db.cardinality/one
                          :db/valueType :db.type/string}
             :block/data {:db/cardinality :db.cardinality/one
                          #_#_:db/valueType :db.type/any
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

(def conn (d/get-conn "./tmp/mydb6" schema
                      {:search-opts {:query-analyzer analyzer
                                     :analyzer analyzer}}))

(comment 
  (d/transact! conn
               [{:block/id "XaHGLadjhT"
                 :block/type "paragraph"
                 :block/data {:text "dhcdhdkwajsdh World"}}
                {:block/id "BY4lWQ--Wo"
                 :block/type "header"
                 :block/data {:text "what the heck"
                              :level 2}}
                {:block/id "1"
                 :block/type "header"
                 :block/data {:text "中文内容是怎么分词的? 测试一下"
                              :level 2}}
                {:block/id "some page"
                 :block/type "page"
                 :block/data {:blocks ["XaHGLadjhT"
                                       "BY4lWQ--Wo"]}}])

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
       "中文")
  
  (d/fulltext-datoms (d/db conn) "文内容是怎么分词的")
  (d/fulltext-datoms (d/db conn) "ck")

  :rcf)