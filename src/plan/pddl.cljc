(ns plan.pddl
  (:require [clojure.spec :as s]
            [plan.pddl.domain :as domain]))

(s/def ::element
  (s/and list?
         (s/cat :name keyword?
                :spec (s/* any?))))

(s/def ::problem
  (s/cat :define define?
         :type-name (s/and list?
                           (s/cat :type (symbol-pred 'problem)
                                  :name symbol?))
         :defs (s/+ ::element)))

(s/def ::define
  (s/and list?
         (s/or :domain domain/spec-name
               :problem ::problem)))

(s/def ::file ::define)
