(ns plan.search.state-space
  (:require [plan.search.protocols :as p]))

(defn action-info
  [action]
  (let [{:keys [:plan.domain.action/name :plan.domain.action/vars]} action]
    (into [name] vars)))

(defn forward
  ([operators state goal]
   (forward operators state goal [] #{} []))
  ([operators state goal plan states info]
   (when (zero? (mod (apply + info) 10))
     (print (str "\r" info "                       ")))
   (if (p/sat? state goal)
     plan
     (when (< (count plan) 13)
       (loop [actions (p/applicable operators state)]
         (when-let [action (first actions)]
           (let [state' (p/transition state action)
                 plan' (conj plan action)]
             (if-not (states state')
               (if-let [plan (forward operators
                                      state'
                                      goal
                                      plan'
                                      (conj states state')
                                      (conj info (count actions)))]
                 plan
                 (recur (next actions)))
               (recur (next actions))))))))))

(defn backward
  ([operators state goal]
   (backward operators state goal []))
  ([operators state goal plan]
   (if (p/sat? state goal)
     plan
     (loop [actions (p/relevant operators goal)]
       (when-let [action (first actions)]
         (let [goal' (p/transition-inv goal action)
               plan' (into [action] plan)]
           (if-let [plan (backward operators state goal' plan')]
             plan
             (recur (next actions)))))))))

(defn lifted-backward
  ([operators state goal]
   (lifted-backward operators state goal []))
  ([operators state goal plan]
   (if (p/sat? state goal)
     plan
     (loop [actions (p/relevant-lifted operators goal)]
       (when-let [[op subst] (first actions)]
         (let [action (subst op)
               goal' (p/transition-inv (subst goal))
               plan' (into [action] (subst plan))]
           (if-let [plan (lifted-backward operators state goal' plan')]
             plan
             (recur (next actions)))))))))
