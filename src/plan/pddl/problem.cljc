(ns plan.pddl.problem
  (:require [clojure.spec :as s]))

(defn symbol-pred
  [n]
  (fn [x]
    (and (symbol? x)
         (-> x
             name
             clojure.string/lower-case
             (= (name n))))))

(s/def ::element
  (s/and list?
         (s/cat :name keyword?
                :spec (s/* any?))))

(s/def ::problem
  (s/cat :define (symbol-pred 'define)
         :type-name (s/and list?
                           (s/cat :type (symbol-pred 'problem)
                                  :name symbol?))
         :defs (s/+ ::element)))

(def spec-name ::problem)
