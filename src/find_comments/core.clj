(ns find-comments.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

; guide for cli-options: https://github.com/clojure/tools.cli/blob/master/src/test/clojure/clojure/tools/cli_test.clj
(def cli-options
  [["-o" "--output DIRECTORY" "Folder where to write the file"
     :missing "output option is missing"]
   ["-t" "--type TYPE" "type of input"
     :missing "type option is missing"]

   ["-h" "--help"]])

(defn -main [& args]
  (let [options (parse-opts args cli-options :strict true :missing true)
         errors? #(not (empty? (:errors %)))
         help? #(:help %)
         print-help #(do (println "usage:") (println (:summary %)))]
         ; (println options)
    (cond
      (errors? options) (do (print-help options) (println (:errors options)))
      (help? options) (print-help options)
      :else (do 
        (println "doing stuff")
        (println options)))))

(defn find-php-files [path]
  (let [selected-file (clojure.java.io/file path)
         children-entries (file-seq selected-file)]
         (letfn [(match-name [regex entry]
                    (not (empty? (re-matches regex (.getName entry))))) ]
          (->> children-entries
                  (filter #(.isFile %))
                  (filter #(match-name #".*\.php" %))))))

(defn find-comments-in [file]
  (with-open [rdr (clojure.java.io/reader (.getAbsolutePath file))]
    (let [r (range)]
      (letfn [(decorate-comment [[line content]]
                   {:line (inc line) :content content :type :single})
                (zip [v] (map vector r v))]
        (->> (line-seq rdr) 
          zip
          (filter #(.contains (second %) "//"))
          (map decorate-comment)
          (into []))))))

