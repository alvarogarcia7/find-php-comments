(ns find-comments.core-test
  (:require [clojure.java.io :refer [file]])
  (:use midje.sweet
          find-comments.core))

(facts "canary tests"
  (fact "truthiness"
    true => true)
  
  (fact "falsiness"
  false => false))

(defn names-for [files]
  (map #(.getName %) files))

(facts "about finding files"
  (fact "do not find files with a bad extension"
    (names-for (find-php-files "dev-resources/bad_extension")) => [])
  (fact "do not find folders even if they match the pattern"
    (names-for (find-php-files "dev-resources/folder_matching_pattern")) => [])
  (fact "find files in the current directory and children"
    (names-for (find-php-files "dev-resources/matching_at_multiple_levels")) => ["file1.php" "file2.php"]))

(facts "about finding comments"
  (fact ""
    (find-comments-in (file "dev-resources/sample_code/file1.php")) => [{:line 3 :content "other code //comment (single line)" :type :single}]))