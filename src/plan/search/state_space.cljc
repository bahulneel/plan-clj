(ns plan.search.state-space)

(declare sat? applicable effect transition relevant transition-inv relevant-lifted)

(defn forward
  ([operators state goal]
   (forward operators state goal []))
  ([operators state goal plan]
   (if (sat? state goal)
     plan
     (loop [actions (applicable operators state)]
       (when-let [action (first actions)]
         (let [state' (transition state action)
               plan' (conj plan action)]
           (if-let [plan (forward operators state' goal plan')]
             plan
             (recur (next actions)))))))))

(defn backward
  ([operators state goal]
   (backward operators state goal []))
  ([operators state goal plan]
   (if (sat? state goal)
     plan
     (loop [actions (relevant operators goal)]
       (when-let [action (first actions)]
         (let [goal' (transition-inv goal action)
               plan' (into [action] plan)]
           (if-let [plan (backward operators state goal' plan')]
             plan
             (recur (next actions)))))))))

(defn lifted-backward
  ([operators state goal]
   (lifted-backward operators state goal []))
  ([operators state goal plan]
   (if (sat? state goal)
     plan
     (loop [actions (relevant-lifted operators goal)]
       (when-let [[op subst] (first actions)]
         (let [action (subst op)
               goal' (transition-inv (subst goal))
               plan' (into [action] (subst plan))]
           (if-let [plan (lifted-backward operators state goal' plan')]
             plan
             (recur (next actions)))))))))
