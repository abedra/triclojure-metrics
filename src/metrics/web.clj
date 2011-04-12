(ns metrics.web
  (:use [ring.adapter.jetty :only (run-jetty)]
        [compojure.core :only (defroutes GET)]
        [hiccup.core :only (html)]
        [hiccup.page-helpers :only (doctype)]
        [metrics.main :only (word-chart)]
        [incanter core stats charts])
  (:import (java.io ByteArrayInputStream ByteArrayOutputStream)))

(defn layout [title & body]
  (html
   (doctype :html5)
   [:head
    [:title title]]
   [:body
    [:div {:id "header"} [:h2 "Hello TriClojure"]]
    [:div {:id "content"} body]]))

(defn render-chart []
  (let [chart (word-chart)
        os (ByteArrayOutputStream.)
        is (do
             (save chart os)
             (ByteArrayInputStream. (.toByteArray os)))]
    is))

(defroutes web
  (GET "/" [] (layout "Hello" "Chart goes here"))
  (GET "/chart" [] {:status 200 :Content-Type "image/png" :body (render-chart)}))

(defn start []
  (run-jetty (var web) {:port 8080 :join? false}))