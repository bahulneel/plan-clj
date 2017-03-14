(ns plan.pddl
  (:require [clojure.spec :as s]
            [plan.pddl.problem :as problem]
            [plan.pddl.domain :as domain]
            [plan.domain]
            [clojure.spec :as s]))


(s/def ::define
  (s/and list?
         (s/or :domain domain/spec-name
               :problem problem/spec-name)))

(s/def ::file
  (s/or :define ::define
        :defines (s/+ ::define)))

(defn read-form
  [form]
  (let [[_ [type def]] (s/conform ::file form)]
    def))


(s/fdef read-form
        :args (s/cat :form ::file)
        :ret (s/or :domain :plan.domain/definition))

(defn read-str
  [s]
  (-> s
      str
      read-string
      read-form))
