(ns plan.search.state-space
  (:require [plan.search.protocols :as p]))

(defn rpg
  ([operators state goal]
   (rpg operators state goal [#{state}]))
  ([operators state goal rpg]
   (if (p/sat? state goal)
     rpg
     (when (< (count rpg) 100)
       (let [actions (p/applicable operators state)
             state' (reduce (fn [s a]
                              (p/transition s a true))
                            state
                            actions)
             rpg' (conj rpg (set actions) state')]
         (when-not (= state state')
           (recur operators state' goal rpg')))))))

(defn forward
  ([operators state goal]
   (forward operators state goal [] #{} []))
  ([operators state goal plan states info]
   (if (p/sat? state goal)
     plan
     (when (< (count info) 7)
       (loop [actions (p/applicable operators state)]
         (if-let [action (first actions)]
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

(defn forward-rpg
  ([operators state goal]
   (when-let [rpg-init (rpg operators state goal)]
     (let [f (count rpg-init)]
       (loop [visited? #{}
              open-list [[f [] state f]]]
         (when-let [[f plan state best] (first open-list)]
           (prn (reduce (fn [m [f plan _ best]]
                          (update-in m [(count plan) best f] (fnil inc 0)))
                        (sorted-map)
                        open-list))
           (if (p/sat? state goal)
             plan
             (let [g (inc (count plan))]
               (recur
                 (conj visited? state)
                 (->> (p/applicable operators state)
                      (keep (fn [action]
                              (let [state (p/transition state action)]
                                (when-not (visited? state)
                                  [state (conj plan action)]))))
                      (keep (fn [[state plan]]
                              (when-let [rpg (rpg operators state goal)]
                                (let [h (count rpg)
                                      f (+ g h)]
                                  [f plan state (min best f)]))))
                      (into (rest open-list))
                      (sort-by (fn [[f plan _ best]]
                                 (let [v (-> plan last :plan.domain.action/vars)
                                       an (- (count v) (count (set v)))]
                                   [best an f])))))))))))))

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
