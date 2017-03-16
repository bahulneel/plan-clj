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

(s/def ::type #{:predicate :action})

(s/def :plan.domain.predicate/name
  keyword?)

(s/def :plan.domain.predicate/vars
  (s/coll-of ::lvar))

(s/def ::predicate
  (s/keys :req [::type :plan.domain.predicate/name]
          :opt [:plan.domain.predicate/vars]))

(s/def :plan.domain.action/name
  keyword?)

(s/def ::arg
  (s/or :lvar ::lvar
        :constant symbol?))

(s/def :plan.domain.action/vars
  (s/coll-of ::arg))

(s/def ::conjunction
  (s/tuple #{::and} (s/cat ::formula (s/* ::formula))))

(s/def ::constraint
  (s/tuple '#{= !=} ::arg ::arg))

(s/def ::negation
  (s/tuple #{::not} ::formula))

(s/def ::formula
  (s/or :predicate ::predicate
        :conjuntion ::conjunction
        :constraint ::constraint
        :negation ::negation))

(s/def :plan.domain.action/precondition
  ::formula)

(s/def :plan.domain.action/effect
  ::formula)

(s/def ::action
  (s/keys :req [::type
                :plan.domain.action/name
                :plan.domain.action/vars
                :plan.domain.action/precondition
                :plan.domain.action/effect]))

(s/def ::actions
  (s/map-of keyword? ::action))

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

(defn action
  [acction-name vars precodition effect]
  (clean-map
    #:plan.domain.action
        {::type        :action
         :name         acction-name
         :vars         vars
         :precondition precodition
         :effect       effect}))

(defn domain
  [domain-name schema actions]
  {::name    domain-name
   ::schema  schema
   ::actions actions})
