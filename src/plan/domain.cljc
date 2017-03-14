(ns plan.domain
  (:require [clojure.spec :as s]))

(defn clean-map
  [m]
  (into {} (filter second) m))

(s/def ::lvar
  (s/with-gen
    (s/and symbol?
           #(nil? (namespace %))
           #(= \? (first (name %))))
    #(s/gen '#{?a ?b ?c ?d ?e ?f ?g ?h ?i ?j ?k ?l ?m})))

(s/def ::name
  keyword?)

(s/def ::type #{:predicate})

(s/def :plan.domain.predicate/name
  keyword?)

(s/def :plan.domain.predicate/vars
  (s/coll-of ::lvar))

(s/def ::predicate
  (s/keys :req [::type :plan.domain.predicate/name]
          :opt [:plan.domain.predicate/vars]))

(s/def ::schema
  (s/map-of keyword? ::predicate))

(s/def ::definition
  (s/keys :req [::name ::schema ::actions]))

(defn predicate
  [predicate-name vars]
  (clean-map
    #:plan.domain.predicate
        {::type :predicate
         :name  predicate-name
         :vars  vars}))

