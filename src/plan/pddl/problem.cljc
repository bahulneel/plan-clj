(ns plan.pddl.problem
  (:require [plan.problem :as problem]
            [clojure.spec :as s]
            [clojure.core.match :as m]))

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
                :goal (s/or :predicate ::predicate
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

(defn arg-xf
  [plan-name]
  (map (fn [n]
         (keyword plan-name (name n)))))

(defn pred>pred
  [plan-name domain-name {:keys [literal objects]}]
  (problem/predicate (keyword domain-name (name literal))
                     (into [] (arg-xf plan-name) objects)))

(defn predicate-xf
  [plan-name domain-name]
  (map (partial pred>pred plan-name domain-name)))

(defn goal-formula
  [plan-name domain-name goal]
  (m/match
    [goal]
    [[:predicate ?p]] (pred>pred plan-name domain-name ?p)
    [[:conj {:preds ?ps}]] [::problem/and
                            (into []
                                  (predicate-xf plan-name domain-name)
                                  ?ps)]))

(defn object-xf
  [plan-name]
  (map (fn [n]
         (let [ident (keyword plan-name (name n))]
           [ident (problem/object ident)]))))

(defn pddl>problem
  [pddl]
  (if (s/valid? ::problem pddl)
    (let [pddl-problem (s/conform ::problem pddl)
          plan-name (name (get-in pddl-problem [:type-name :name]))
          domain-name (name (get-in pddl-problem [:domain :name]))
          init (into []
                     (predicate-xf plan-name domain-name)
                     (get-in pddl-problem [:init :predicates]))
          goal (goal-formula plan-name
                             domain-name
                             (get-in pddl-problem [:goal :goal]))
          objects (into {}
                        (object-xf plan-name)
                        (get-in pddl-problem [:objects :objects]))]
      (problem/problem (keyword plan-name)
                       (keyword domain-name)
                       objects
                       init
                       goal))))

(s/fdef pddll>problem
        :args (s/cat :problem ::problem)
        :ret ::problem/definition)
