(ns reify2.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [clojure.data.json :as json]))


(defn filter-payload
   "Transform a request with JSON-params payload into a map of select values (action taken, parameters)"
   [request]
   (let [json-response (-> request :json-params)
         {:keys [result]} json-response
         interesting-stuff (select-keys result [:action :parameters])]
     interesting-stuff))

(defn respond-echo
  "Respond to an API.ai request, returning a simple \'echo\' speech response"
  [request]
  (println request)
  (let [{:keys [action parameters]} (filter-payload request)]
    {:status 200
     :body (json/write-str {"speech" (str "Performing " action " with parameters " (pr-str parameters))})
     :headers {"Content-Type" "application/json"}}))


(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/echo" :post (conj common-interceptors `respond-echo)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false}})

