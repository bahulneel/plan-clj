(ns plan.problem
  (:require [clojure.spec :as s]
            [clojure.spec :as s]))

(s/def ::name
  keyword?)

(s/def ::domain
  keyword?)

(s/def ::type #{:object :predicate})

(s/def ::predicate
  (s/keys :req [::type :plan.problem.predicate/name :plan.problem.predicate/args]))

(s/def ::init
  (s/coll-of ::predicate))

(s/def ::conjuntion
  (s/tuple #{::and} (s/coll-of ::predicate)))

(s/def ::goal
  (s/or :conj ::conjuntion
        :pred ::predicate))


(s/def ::object
  (s/keys :req [::type ::ident]))

(s/def ::entity
  (s/or :object ::object))

(s/def ::schema
  (s/map-of keyword? ::entity))

(s/def ::definition
  (s/keys :req [::name ::domain ::schema ::init ::goal]))

(defn object
  [ident]
  {::type  :object
   ::ident ident})

(defn predicate
  [predicate args]
  #:plan.problem.predicate
      {::type :predicate
       :name  predicate
       :args  args})

(defn problem
  [name domain schema init goal]
  {::name   name
   ::domain domain
   ::schema schema
   ::init   init
   ::goal   goal})

(defn init
  [problem domain]
  (when (= (:domain problem) (:name domain))
    {:plan/problem problem
     :plan/domain  domain}))


