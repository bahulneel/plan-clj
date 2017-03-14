(ns plan.pddl.domain
  (:require [plan.domain :as domain]
            [clojure.spec :as s]))

(defmacro symbol-pred
  [s]
  (let [n (name s)
        n-lower (symbol (clojure.string/lower-case n))
        n-upper (symbol (clojure.string/upper-case n))]
    `'#{~n-lower ~n-upper}))

(s/def ::lvar
  ::domain/lvar)

(s/def ::requirements
  (s/and list?
         (s/cat :type #{:requirements}
                :requirements (s/* keyword?))))

(s/def ::constants
  (s/and list?
         (s/cat :type #{:constants}
                :constants (s/* symbol?))))

(s/def ::predicate
  (s/and list?
         (s/cat :predicate symbol?
                :vars (s/* ::lvar))))

(s/def ::predicates
  (s/and list?
         (s/cat :type #{:predicates}
                :predicates (s/* ::predicate))))

(s/def ::parameters
  (s/cat :type #{:parameters}
         :parameters (s/coll-of ::lvar :kind list)))

(s/def ::constraint
  (s/and list?
         (s/cat :type #{'= '!=}
                :args (s/cat :lhs symbol? :rhs symbol?))))

(s/def ::atom
  (s/or :constant symbol?
        :constraint ::constraint
        :predicate (s/and list?
                          (s/cat :predicate symbol?
                                 :args (s/* ::lvar)))))

(s/def ::neg-atom
  (s/and list?
         (s/cat :type #{'not}
                :atom ::atom)))

(s/def ::atom-formula
  (s/or :atom ::atom
        :neg-atom ::neg-atom))

(s/def ::conj
  (s/and list?
         (s/cat :type #{'and}
                :atoms (s/+ ::atom-formula))))

(s/def ::precondition
  (s/cat :type #{:precondition}
         :precondition (s/or :conj ::conj
                             :atom ::atom-formula)))

(s/def ::effect
  (s/cat :type #{:effect}
         :effect (s/or :atom ::atom
                       :conj ::conj)))

(s/def ::duration
  (s/cat :type #{:duration}
         :duration (s/and integer? pos?)))

(s/def ::action
  (s/and list?
         (s/cat :type #{:action}
                :name symbol?
                :parameters (s/? ::parameters)
                :precondition (s/? ::precondition)
                :effect (s/? ::effect)
                :duration (s/? ::duration))))

(s/def ::domain
  (s/cat :define (symbol-pred define)
         :domain (s/and list?
                        (s/cat :domain (symbol-pred domain)
                               :name symbol?))
         :requirements (s/? ::requirements)
         :constants (s/? ::constants)
         :predicates ::predicates
         :actions (s/+ ::action)))

(def spec-name ::domain)

(s/fdef pddll>domain
        :args (s/cat :domain ::domain)
        :ret :plan.domain/definition)

(defn pddl>domain
  [pddl]
  (if (s/valid? ::domain pddl)
    (let [pddl-domain (s/conform ::domain pddl)
          domain-name (name (get-in pddl-domain [:domain :name]))
          preds (into {}
                      (comp
                        (map (fn [{:keys [predicate vars]}]
                               (let [predicate-name (keyword domain-name
                                                             (name predicate))]
                                 [predicate-name
                                  (domain/predicate predicate-name vars)]))))
                      (get-in pddl-domain [:predicates :predicates]))
          actions []]
      (prn (first (get-in pddl-domain [:actions])))
      {::domain/name    (keyword domain-name)
       ::domain/schema  preds
       ::domain/actions actions})))
