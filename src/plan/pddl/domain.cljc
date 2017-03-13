(ns plan.pddl.domain
  (:require [clojure.spec :as s]))

(defn symbol-pred
  [n]
  (fn [x]
    (and (symbol? x)
         (-> x
             name
             clojure.string/lower-case
             (= (name n))))))

(s/def ::lvar
  (s/and symbol?
         #(= \? (first (name %)))))

(s/def ::requirements
  (s/and list?
         (s/cat :type #(= % :requirements)
                :requirements (s/* keyword?))))

(s/def ::constants
  (s/and list?
         (s/cat :type #(= % :constants)
                :constants (s/* symbol?))))

(s/def ::predicate
  (s/and list?
         (s/cat :predicate symbol?
                :vars (s/* ::lvar))))

(s/def ::predicates
  (s/and list?
         (s/cat :type #(= % :predicates)
                :predicates (s/* ::predicate))))

(s/def ::parameters
  (s/cat :type #(= % :parameters)
         :parameters (s/coll-of ::lvar :kind list)))

(s/def ::equality
  (s/and list?
         (s/cat :equality #(= % '=)
                :args (s/cat :lhs symbol? :rhs symbol?))))

(s/def ::negated-equality
  (s/and list?
         (s/cat :negation #(= % 'not)
                :equality ::equality)))

(s/def ::pos-atom
  (s/or :constant symbol?
        :atom (s/and list?
               (s/cat :predicate symbol?
                      :args (s/* ::lvar)))))

(s/def ::neg-atom
  (s/and list?
         (s/cat :conj #(= % 'not)
                :atom ::pos-atom)))

(s/def ::atom
  (s/or :pos-atom ::pos-atom
        :neg-atom ::neg-atom
        :equality ::equality
        :negated-equality ::negated-equality))

(s/def ::conj
  (s/and list?
         (s/cat :type #(= % 'and)
                :atoms (s/+ ::atom))))

(s/def ::precondition
  (s/cat :type #(= % :precondition)
         :precondition (s/or :conj ::conj
                             :atom ::atom)))

(s/def ::effect
  (s/cat :type #(= % :effect)
         :effect (s/or :atom ::atom
                       :conj ::conj)))

(s/def ::duration
  (s/cat :type #(= % :duration)
         :duration integer?))

(s/def ::action
  (s/and list?
         (s/cat :type #(= % :action)
                :name symbol?
                :parameters (s/? ::parameters)
                :precondition (s/? ::precondition)
                :effect (s/? ::effect)
                :duration (s/? ::duration))))

(s/def ::domain
  (s/cat :define (symbol-pred 'define)
         :domain (s/and list?
                      (s/cat :domain (symbol-pred 'domain)
                             :name symbol?))
         :requirements (s/? ::requirements)
         :constants (s/? ::constants)
         :predicates ::predicates
         :actions (s/+ ::action)))

(def spec-name ::domain)
