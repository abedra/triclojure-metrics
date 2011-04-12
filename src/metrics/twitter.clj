(ns metrics.twitter
  (:use [clojure.data.json :only [read-json]]
        [clojure-http.client :only [request]]))

(defn from-twitter
  []
  (read-json
   (apply str
          (:body-seq (request "http://twitter.com/statuses/public_timeline.json")))))

(defn from-file
  []
  (read-json
   (slurp "data/public_timeline.json")))

(defn fetch-public-timeline
  "Retrieves and parses the JSON formatted public timeline. If no internet connection
   is present or the public timeline cannot be parsed then pull it from a local file
   in the project."
  []
  (try
    (from-twitter)
    (catch Exception e
      (from-file))))

(defn text
  "Extracts tweet text"
  [msg]
  (:text msg))

(defn user
  "Extracts tweet username"
  [msg]
  (str "@" (:screen_name (:user msg))))

(defn public-timeline
  "Prints out the public timeline. Not really useful for anything but debugging.
   Use fetch-public-timeline for real results"
  []
  (doseq [tweet (fetch-public-timeline)]
    (println (user tweet))
    (println (str (text tweet) "\n"))))
