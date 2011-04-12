(ns metrics.main
  (:use [metrics.twitter :only (fetch-public-timeline text)]
        [incanter core stats charts]))

(defn- create-word-list []
  (let [tweets (fetch-public-timeline)
        words '()]
    (for [tweet tweets]
      (conj words (text tweet)))))

(defn words []
  (re-seq #"\w+" (apply str (flatten (create-word-list)))))

(defn word-chart []
  (let [word-freq (frequencies (words))
        words (keys word-freq)
        frequencies (filter #(< 1 %)
                            (map word-freq words))]
    (bar-chart words frequencies :vertical false)))