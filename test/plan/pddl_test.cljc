(ns plan.pddl-test
  (:require [plan.pddl :as sut]
            [clojure.spec :as s]
    #?(:clj
            [me.raynes.fs :as fs])
    #?(:clj
            [clojure.test :as t]
       :cljs [cljs.test :as t :include-macros true])))

(def files #?(:clj  (mapcat fs/glob ["./examples/strips/domain/*.pddl"
                                     "./examples/strips/problem/*.pddl"])
              :cljs []))

(t/deftest parsing
  (doseq [file files]
    (t/testing (str "Validity of " file)
      (let [file-str (slurp file)
            src (read-string file-str)]
        (t/is (true? (or (s/valid? :plan.pddl/file src)
                         (do (println (str "In: " file))
                             (s/explain :plan.pddl/file src)
                             (s/explain-data :plan.pddl/file src)))))))))
