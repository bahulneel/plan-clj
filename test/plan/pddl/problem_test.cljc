(ns plan.pddl.problem-test
  (:require [plan.pddl.problem :as sut]
            [plan.problem :as problem]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]
    #?(:clj
            [me.raynes.fs :as fs])
    #?(:clj
            [clojure.test :as t]
       :cljs [cljs.test :as t :include-macros true])))

(def files #?(:clj  (fs/glob "./examples/strips/problem/*.pddl")
              :cljs []))

(stest/instrument `plan.pddl.domain)

(t/deftest parsing
  (doseq [file files]
    (t/testing (str "Validity of " file)
      (let [file-str (slurp file)
            src (read-string file-str)
            problem (sut/pddl>problem src)]
        (t/is (true? (or (s/valid? ::problem/definition problem)
                         (do (println (str "In: " file))
                             (s/explain ::problem/definition problem)
                             (s/explain-data ::problem/definition problem)))))))))
