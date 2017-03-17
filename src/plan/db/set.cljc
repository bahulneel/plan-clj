(ns plan.db.set
  (:require [plan.domain :as domain]
            [plan.problem :as problem]
            [clojure.core.unify :as u]
            [plan.search.protocols :as search]
            [clojure.spec :as s]))

(s/def ::fact
  (s/coll-of keyword?))

(s/def ::state
  (s/coll-of ::fact :kind set?))

(s/def ::domain
  ::domain/definition)

(s/def ::init
  ::state)

(s/def ::goal
  ::state)

(s/def ::db
  (s/keys :opt [::domain ::init ::goal]))

(defn make-db
  []
  {})

(s/fdef make-db
        :ret ::db)

(defn add-domain
  [db domain]
  (assoc db ::domain domain))

(s/fdef add-domain
        :args (s/cat :db ::db
                     :domain ::domain/definition)
        :ret ::db)

(defn apply-schema
  [db schema]
  db)

(s/fdef apply-schema
        :args (s/cat :db ::db
                     :schema ::problem/schema)
        :ret ::db)

(defn init>state
  [db init]
  (into (sorted-set)
        (map (fn [{:keys [:plan.problem.predicate/name :plan.problem.predicate/args]}]
               (into [name] args)))
        init))

(s/fdef init>state
        :args (s/cat :db ::db
                     :init ::problem/init)
        :ret ::state)

(defn goal>state
  [db goal]
  (if (= ::problem/and (first goal))
    (init>state db (second goal))
    (init>state db [goal])))

(s/fdef goal>state
        :args (s/cat :db ::db
                     :init ::problem/goal)
        :ret ::state)

(defn transition
  [state action]
  (let [state' state]
    state'))

(s/fdef transition
        :args (s/cat :state ::state
                     :action ::domain/action)
        :ret ::state)

(defn init
  [db problem]
  (let [domain-name (get-in db [::domain ::domain/name])]
    (if-not (= (::problem/domain problem) domain-name)
      (throw (ex-info "Problem domain does not match db domain"
                      {:domain domain-name}))
      (let [db' (apply-schema db (::problem/schema problem))
            init-state (init>state db' (::problem/init problem))
            goal-state (goal>state db' (::problem/goal problem))]
        (-> db'
            (assoc ::init init-state)
            (assoc ::goal goal-state))))))

(s/fdef init
        :args (s/cat :db ::db :problem ::problem/definition)
        :ret ::db)

(defrecord State [state]
  search/State
  (-sat? [_ goal]
    (empty? (clojure.set/difference state (:state goal))))
  (-transition [this action]
    (let [{:keys [:plan.domain.action/effect]} action
          [pos neg] (domain/rels effect)
          state' (-> state
                     (clojure.set/difference neg)
                     (clojure.set/union pos))]
      (->State state'))))

(defn unify-precond
  [[n action] state]
  (let [{:keys [:plan.domain.action/precondition]} action
        bindings (domain/unify-formula precondition (:state state))]
    (map (fn [b]
           (u/subst action b))
         bindings)))

(defrecord Domain [domain]
  search/Search
  (-applicable [_ state]
    (->> domain
         ::domain/actions
         (mapcat #(unify-precond % state)))))

(defn state-space-search
  [db planner]
  (let [{:keys [::domain ::init ::goal]} db]
    (planner (->Domain domain)
             (->State init)
             (->State goal))))
