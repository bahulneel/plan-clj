(ns plan.pddl.domain-test
  (:require [plan.pddl.domain :as sut]
            [plan.domain :as domain]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]
    #?(:clj
            [me.raynes.fs :as fs])
    #?(:clj
            [clojure.test :as t]
       :cljs [cljs.test :as t :include-macros true])))

(def files #?(:clj  (fs/glob "./examples/strips/domain/*.pddl")
              :cljs []))

(t/deftest parsing
  (doseq [file files]
    (t/testing (str "Validity of " file)
      (let [file-str (slurp file)
            src (read-string file-str)
            domain (sut/pddl>domain src)]
        (t/is (true? (or (s/valid? ::domain/definition domain)
                         (do (println (str "In: " file))
                             (s/explain ::domain/definition domain)
                             (s/explain-data ::domain/definition domain)))))))))
