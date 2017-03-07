(ns plan.pddl
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

(s/def ::element
  (s/and list?
         (s/cat :name keyword?
                :spec (s/* any?))))

(s/def ::problem
  (s/cat :define define?
         :type-name (s/and list?
                           (s/cat :type (symbol-pred 'problem)
                                  :name symbol?))
         :defs (s/+ ::element)))

(s/def ::requirements
  (s/and list?
         (s/cat :name #(= % :requirements)
                :reqs (s/* keyword?))))

(s/def ::constants
  (s/and list?
         (s/cat :name #(= % :constants)
                :reqs (s/* symbol?))))

(s/def ::predicate
  (s/and list?
         (s/cat :name symbol?
                :args (s/* ::lvar))))

(s/def ::predicates
  (s/and list?
         (s/cat :name #(= % :predicates)
                :preds (s/* ::predicate))))

(s/def ::parameters
  (s/cat :name #(= % :parameters)
         :params (s/coll-of ::lvar :kind list)))

(s/def ::equality
  (s/and list?
         (s/cat :equality #(= % '=)
                :args (s/cat :lhs symbol? :rhs symbol?))))

(s/def ::negated-equality
  (s/and list?
         (s/cat :negation #(= % 'not)
                :equality ::equality)))


(s/def ::precond-formula
  (s/or :neg-equality ::negated-equality
        :equality ::equality
        :precond (s/and list?
                        (s/cat :predicate symbol?
                               :args (s/* ::lvar)))))

(s/def ::precond-conj
  (s/and list?
         (s/cat :conj #(= % 'and)
                :atoms (s/+ ::precond-formula))))

(s/def ::precondition
  (s/cat :name #(= % :precondition)
         :precondition (s/or :conj ::precond-conj
                             :atom ::precond-formula)))

(s/def ::effect-pos-atom
  (s/and list?
         (s/cat :predicate symbol?
                :args (s/* ::lvar))))

(s/def ::effect-neg-atom
  (s/and list?
         (s/cat :conj #(= % 'not)
                :atom ::effect-pos-atom)))

(s/def ::effect-atom
  (s/or :pos-atom ::effect-pos-atom
        :neg-atom ::effect-neg-atom))

(s/def ::effect-conj
  (s/and list?
         (s/cat :conj #(= % 'and)
                :atoms (s/+ ::effect-atom))))

(s/def ::effect
  (s/cat :name #(= % :effect)
         :effect (s/or :atom ::effect-atom
                       :conj ::effect-conj)))

(s/def ::duration
  (s/cat :name #(= % :duration)
         :duration integer?))

(s/def ::action
  (s/and list?
         (s/cat :name #(= % :action)
                :action-name symbol?
                :parameters (s/? ::parameters)
                :precondition (s/? ::precondition)
                :effect (s/? ::effect)
                :duration (s/? ::duration))))

(s/def ::domain
  (s/cat :define (symbol-pred 'define)
         :type-name (s/and list?
                           (s/cat :type (symbol-pred 'domain)
                                  :name symbol?))
         :require (s/? ::requirements)
         :constants (s/? ::constants)
         :predicates ::predicates
         :actions (s/+ ::action)))

(s/def ::define
  (s/and list?
         (s/or :domain ::domain
               :problem ::problem)))

(s/def ::file ::define)
