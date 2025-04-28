(ns com.linzihao.local-file.interface
  (:require [com.linzihao.local-file.core :as core]))

(def tools
  [{:type "function"
    :function
    {:name "search-note"
     :description "Search for notes that match the given keyword. return a list of note filenames"
     :parameters
     {:type "object"
      :properties {:keyword {:type "string"}}
      :required ["keyword"]
      :additionalProperties false}
     :strict true}}

   {:type "function"
    :function
    {:name "read-note"
     :description "Read the content of a note."
     :parameters
     {:type "object"
      :properties {:filename {:type "string"
                             :description "The filename of the note to read"}}
      :required ["filename"]
      :additionalProperties false}
     :strict true}}

   {:type "function"
    :function
    {:name "add-note"
     :description "when user tell you something factual or explicit ask you to remember something, use this function. the content should be concise."
     :parameters
     {:type "object"
      :properties {:filename {:type "string"}
                   :content {:type "string"}}
      :required ["filename" "content"]
      :additionalProperties false}
     :strict true}}


   {:type "function"
    :function
    {:name "update-note"
     :description "Update the content of an existing note."
     :parameters
     {:type "object"
      :properties {:filename {:type "string"}
                   :new-content {:type "string"}}
      :required ["filename" "new-content"]
      :additionalProperties false}
     :strict true}}])

(def tool->f
  {"search-note" core/search-note
   "read-note" core/read-note
   "add-note" core/add-note
   "modify-note" core/update-note})