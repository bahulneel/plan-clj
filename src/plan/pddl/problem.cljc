(ns plan.pddl.problem
  (:require [clojure.spec :as s]))

(defn symbol-pred
  [n]
  (fn [x]
    (and (symbol? x)
         (-> x
             name
             clojure.string/lower-case
             (= (name n))))))

(defn kw-pred
  [n]
  (fn [x]
    (and (keyword? x)
         (-> x
             name
             clojure.string/lower-case
             (= (name n))))))

(s/def ::domain
  (s/and list?
         (s/cat :key (kw-pred :domain)
                :name symbol?)))

(s/def ::requirements
  (s/and list?
         (s/cat :name #(= % :requirements)
                :reqs (s/* keyword?))))

(s/def ::object-decl
  (s/and list?
         (s/cat :key (kw-pred :objects)
                :objects (s/+ symbol?))))

(s/def ::predicate
  (s/and list?
         (s/cat :literal symbol?
                :objects (s/* symbol?))))

(s/def ::init
  (s/and list?
         (s/cat :key (kw-pred :init)
                :predicates (s/+ ::predicate))))

(s/def ::conjunction
  (s/and list?
         (s/cat :conj (symbol-pred 'and)
                :preds (s/+ ::predicate))))

(s/def ::goal
  (s/and list?
         (s/cat :key (kw-pred :goal)
                :goal (s/or :pred ::predicate
                            :conj ::conjunction))))

(s/def ::problem
  (s/cat :define (symbol-pred 'define)
         :type-name (s/and list?
                           (s/cat :type (symbol-pred 'problem)
                                  :name symbol?))
         :domain ::domain
         :require (s/? ::requirements)
         :objects (s/? ::object-decl)
         :init (s/? ::init)
         :goal ::goal))

(def spec-name ::problem)
