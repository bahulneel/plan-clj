(ns plan.search.protocols)

(defprotocol State
  (-sat? [state goal])
  (-transition [state action pos-only?])
  (-transition-inv [state action]))

(defn state?
  [state]
  (satisfies? State state))

(defn sat?
  [state goal]
  (-sat? state goal))

(defn transition
  ([state action]
   (transition state action false))
  ([state action pos-only?]
   (-transition state action pos-only?)))

(defn transition-inv
  [state action]
  (-transition-inv state action))

(defprotocol Search
  (-applicable [operators state])
  (-relevant [operators state]))

(defn applicable
  [operators state]
  (-applicable operators state))

(defn relevant
  [operators state]
  (-relevant operators state))

(defprotocol Lifted
  (-applicable-lifted [operators state])
  (-relevant-lifted [operators state]))

(defn applicable-lifted
  [operators state]
  (-applicable-lifted operators state))

(defn relevant-lifted
  [operators state]
  (-relevant-lifted operators state))
