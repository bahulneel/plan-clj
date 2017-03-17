(ns plan.db.set-test
  (:require [plan.db.set :as sut]
            [plan.example.bw :as bw]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]
    #?(:clj
            [clojure.test :as t]
       :cljs [cljs.test :as t :include-macros true])))

(stest/instrument)

(t/deftest testing-loading
  (t/testing "Initialising a program"
    (let [db (-> (sut/make-db)
                 (sut/add-domain bw/domain)
                 (sut/init bw/problem))]
      (t/is (true? (or (s/valid? ::sut/db db)
                       (do (s/explain ::sut/db db)
                           (s/explain-data ::sut/db db))))))))



