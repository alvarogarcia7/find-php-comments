(ns find-comments.core-test
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
  (fact "find files in the current directory and children"
    (names-for (find-php-files "dev-resources")) => ["file1.php" "file2.php"]))