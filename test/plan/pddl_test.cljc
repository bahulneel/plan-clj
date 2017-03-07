(ns plan.pddl-test
  (:require [plan.pddl :as sut]
            [me.raynes.fs :as fs]
            [clojure.spec :as s]
            #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t :include-macros true])))

(def files (fs/glob "./examples/strips/*.pddl"))

(t/deftest parsing
  (doseq [file files]
    (t/testing (str "Validity of " file)
      (let [str (slurp file)
            src (read-string str)]
        (t/is (true? (or (s/valid? :plan.pddl/file src)
                         (do (s/explain :plan.pddl/file src)
                             (s/explain-data :plan.pddl/file src)))))))))
