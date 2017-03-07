(ns plan.pddl
  (:require [clojure.spec :as s]
            [plan.pddl.problem :as problem]
            [plan.pddl.domain :as domain]))


(s/def ::define
  (s/and list?
         (s/or :domain domain/spec-name
               :problem problem/spec-name)))

(s/def ::file
  (s/or :define ::define
        :defines (s/+ ::define)))
