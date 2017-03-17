(ns plan.domain
  (:require [clojure.spec :as s]
            [clojure.set]
            [clojure.core.unify :as u]))


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
  [action-name vars precodition effect]
  (clean-map
    #:plan.domain.action
        {::type        :action
         :name         action-name
         :vars         vars
         :precondition precodition
         :effect       effect}))

(defn domain
  [domain-name schema actions]
  {::name    domain-name
   ::schema  schema
   ::actions actions})

(defn formula-type
  [formula _ _]
  (if (sequential? formula)
    (first formula)
    (::type formula)))

(defn conflict?
  [x y]
  (let [x-ks (set (keys x))
        y-ks (set (keys y))
        ks (clojure.set/intersection x-ks y-ks)]
    (if (seq ks)
      (let [x' (select-keys x ks)
            y' (select-keys y ks)
            conflict? (not= x' y')]
        conflict?)
      false)))

(defmulti unify* #'formula-type)

(defmethod unify* ::and
  [[_ fs] rels binding]
  (reduce (fn [xs ys]
            (into #{}
                  (remove nil?
                          (for [x xs
                                y ys
                                :when (not (conflict? x y))]
                            (merge x y)))))
          #{{}}
          (map #(unify* % rels binding) fs)))

(defmethod unify* :predicate
  [p rels binding]
  (into #{}
        (keep (fn [state]
                (let [{:keys [:plan.domain.predicate/name :plan.domain.predicate/vars]} p
                      rel (u/subst (into [name] vars) binding)
                      b (u/unify state rel)]
                  (when b
                    (merge binding b)))))
        rels))

(defn unify-formula
  [formula rels]
  (unify* formula rels {}))

(defmulti rels* #'formula-type)

(defmethod rels* ::and
  [[_ fs] rels neg?]
  (reduce (fn [rels f]
            (rels* f rels neg?))
          rels
          fs))

(defmethod rels* :predicate
  [p rels neg?]
  (let [{:keys [:plan.domain.predicate/name :plan.domain.predicate/vars]} p
        rel (into [name] vars)]
    (update rels (if neg? 1 0) conj rel)))

(defmethod rels* ::not
  [[_ f] rels neg?]
  (rels* f rels true))

(defn rels
  [formula]
  (rels* formula [#{} #{}] false))

