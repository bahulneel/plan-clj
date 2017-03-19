(ns plan.db.set-test
  (:require [plan.db.set :as sut]
            [plan.example.hanoi :as hanoi]
            [plan.example.bw :as bw]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]
            [plan.search.state-space :as ss]
    #?(:clj
            [clojure.test :as t]
       :cljs [cljs.test :as t :include-macros true])))

(stest/instrument)

(t/deftest testing-loading
  (t/testing "Initialising a program"
    (let [db (-> (sut/make-db)
                 (sut/add-domain hanoi/domain)
                 (sut/init hanoi/problem))]
      (t/is (true? (or (s/valid? ::sut/db db)
                       (do (s/explain ::sut/db db)
                           (s/explain-data ::sut/db db))))))))

(defn action-info
  [action]
  (let [{:keys [:plan.domain.action/name :plan.domain.action/vars]} action]
    (into [name] vars)))

(t/deftest testing-hanoi
  (let [db (-> (sut/make-db)
               (sut/add-domain hanoi/domain)
               (sut/init hanoi/problem))]
    (t/testing "forward planning"
      (let [plan (time (sut/state-space-search db ss/forward))]
        (clojure.pprint/pprint (map action-info plan))
        (t/is (not (empty? plan)))))
    (t/testing "forward planning (rpg)"
      (let [plan (time (sut/state-space-search db ss/forward-rpg))]
        (clojure.pprint/pprint (map action-info plan))
        (t/is (not (empty? plan)))))))

(t/deftest testing-bw
  (let [db (-> (sut/make-db)
               (sut/add-domain bw/domain)
               (sut/init bw/problem))]
    (t/testing "forward planning"
      (let [plan (time (sut/state-space-search db ss/forward))]
        (clojure.pprint/pprint (map action-info plan))
        (t/is (not (empty? plan)))))
    (t/testing "forward planning (rpg)"
      (let [plan (time (sut/state-space-search db ss/forward-rpg))]
        (clojure.pprint/pprint (map action-info plan))
        (t/is (not (empty? plan)))))))
