(ns com.linzihao.local-file.core
  (:require
   [clojure.string :as str]
   [babashka.fs :as fs]
   [babashka.process :as process]))

(def data-path "/home/zihao/workspace/private/agent-from-scratch/data")

(defn print-tree
  "Recursively prints the directory tree starting from `dir`."
  ([dir] (print-tree dir ""))
  ([dir prefix]
   (let [entries (sort (fs/list-dir dir))]
     (doseq [[idx entry] (map-indexed vector entries)]
       (let [basename (fs/file-name entry)
             is-last (= idx (dec (count entries)))
             connector (if is-last "└── " "├── ")
             new-prefix (str prefix (if is-last "    " "│   "))]
         (println (str prefix connector basename))
         (when (fs/directory? entry)
           (print-tree entry new-prefix)))))))

(comment
  (print-tree ".")
  :rcf)

(defn file-matches? [filename keywords]
  (let [norm-name (fs/canonicalize filename)]
    (every? #(str/includes? norm-name %) keywords)))

(comment
  (fs/canonicalize ".")
  (file-matches? "." ["clojure"])
  (file-matches? "." ["data"])
  (file-matches? (str data-path "/clojure") ["clojure"])
  :rcf)

(defn find-matching-files
  "Returns a list of file paths in `dir` whose names fuzzily match all keywords."
  [dir keyword-str]
  (let [keywords (str/split keyword-str #"\s+")]
    (->> (concat (fs/glob dir "*") (fs/glob dir "**/*"))
         (filter fs/regular-file?)
         (filter #(file-matches? (fs/canonicalize %) keywords))
         (map str)
         (into []))))

(comment 
  (find-matching-files data-path "inner")
  :rcf)


