(ns com.linzihao.openai-client.clients
  (:require
   [clojure.java.io :as io]
   [aero.core :refer [read-config]]))

(def config
  (read-config (io/resource "openai-client/config.edn")))

(def akash-client {:base-url "https://chatapi.akash.network/api/v1"
                   :api-key (get-in config [:secrets :akash-apikey])
                   :model "DeepSeek-R1"
                   :tools []})

(def deepseek-client {:base-url "https://api.deepseek.com"
                      :api-key (get-in config [:secrets :deepseek-apikey])
                      :model "deepseek-chat"
                      :tools []})

(def openrouter-client {:base-url "https://openrouter.ai/api/v1"
                        :api-key (get-in config [:secrets :openrouter-apikey])
                        :model "qwen/qwen3-235b-a22b:free"
                        :tools []})