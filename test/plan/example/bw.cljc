(ns plan.example.bw)

(def domain
  '#:plan.domain{:name :blocks_world,
                 :schema
                       #:blocks_world{:on-table
                                      {:plan.domain/type           :predicate,
                                       :plan.domain.predicate/name
                                                                   :blocks_world/on-table,
                                       :plan.domain.predicate/vars [?x]},
                                      :on
                                      {:plan.domain/type           :predicate,
                                       :plan.domain.predicate/name
                                                                   :blocks_world/on,
                                       :plan.domain.predicate/vars [?x ?y]},
                                      :clear
                                      {:plan.domain/type           :predicate,
                                       :plan.domain.predicate/name
                                                                   :blocks_world/clear,
                                       :plan.domain.predicate/vars [?x]}},
                 :actions
                       #:blocks_world{:MoveToTable
                                      {:plan.domain/type        :action,
                                       :plan.domain.action/name
                                                                :blocks_world/MoveToTable,
                                       :plan.domain.action/vars (?x ?y),
                                       :plan.domain.action/precondition
                                                                [:plan.domain/and
                                                                 [{:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/clear,
                                                                   :plan.domain.predicate/vars [?x]}
                                                                  {:plan.domain/type :predicate,
                                                                   :plan.domain.predicate/name
                                                                                     :blocks_world/on,
                                                                   :plan.domain.predicate/vars
                                                                                     [?x ?y]}]],
                                       :plan.domain.action/effect
                                                                [:plan.domain/and
                                                                 [{:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/clear,
                                                                   :plan.domain.predicate/vars [?y]}
                                                                  {:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/on-table,
                                                                   :plan.domain.predicate/vars [?x]}
                                                                  [:plan.domain/not
                                                                   {:plan.domain/type :predicate,
                                                                    :plan.domain.predicate/name
                                                                                      :blocks_world/on,
                                                                    :plan.domain.predicate/vars
                                                                                      [?x ?y]}]]]},
                                      :MoveToBlock1
                                      {:plan.domain/type        :action,
                                       :plan.domain.action/name
                                                                :blocks_world/MoveToBlock1,
                                       :plan.domain.action/vars (?x ?y ?z),
                                       :plan.domain.action/precondition
                                                                [:plan.domain/and
                                                                 [{:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/clear,
                                                                   :plan.domain.predicate/vars [?x]}
                                                                  {:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/clear,
                                                                   :plan.domain.predicate/vars [?z]}
                                                                  {:plan.domain/type :predicate,
                                                                   :plan.domain.predicate/name
                                                                                     :blocks_world/on,
                                                                   :plan.domain.predicate/vars
                                                                                     [?x ?y]}]],
                                       :plan.domain.action/effect
                                                                [:plan.domain/and
                                                                 [{:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/clear,
                                                                   :plan.domain.predicate/vars [?y]}
                                                                  {:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/on,
                                                                   :plan.domain.predicate/vars [?x ?z]}
                                                                  [:plan.domain/not
                                                                   {:plan.domain/type           :predicate,
                                                                    :plan.domain.predicate/name
                                                                                                :blocks_world/clear,
                                                                    :plan.domain.predicate/vars [?z]}]
                                                                  [:plan.domain/not
                                                                   {:plan.domain/type :predicate,
                                                                    :plan.domain.predicate/name
                                                                                      :blocks_world/on,
                                                                    :plan.domain.predicate/vars
                                                                                      [?x ?y]}]]]},
                                      :MoveToBlock2
                                      {:plan.domain/type        :action,
                                       :plan.domain.action/name
                                                                :blocks_world/MoveToBlock2,
                                       :plan.domain.action/vars (?x ?y),
                                       :plan.domain.action/precondition
                                                                [:plan.domain/and
                                                                 [{:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/clear,
                                                                   :plan.domain.predicate/vars [?x]}
                                                                  {:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/clear,
                                                                   :plan.domain.predicate/vars [?y]}
                                                                  {:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/on-table,
                                                                   :plan.domain.predicate/vars [?x]}]],
                                       :plan.domain.action/effect
                                                                [:plan.domain/and
                                                                 [{:plan.domain/type           :predicate,
                                                                   :plan.domain.predicate/name
                                                                                               :blocks_world/on,
                                                                   :plan.domain.predicate/vars [?x ?y]}
                                                                  [:plan.domain/not
                                                                   {:plan.domain/type           :predicate,
                                                                    :plan.domain.predicate/name
                                                                                                :blocks_world/clear,
                                                                    :plan.domain.predicate/vars [?y]}]
                                                                  [:plan.domain/not
                                                                   {:plan.domain/type :predicate,
                                                                    :plan.domain.predicate/name
                                                                                      :blocks_world/on-table,
                                                                    :plan.domain.predicate/vars
                                                                                      [?x]}]]]}}})

(def problem
  '#:plan.problem{:name   :bw-12step,
                  :domain :blocks_world,
                  :schema
                          #:bw-12step{:A
                                      #:plan.problem{:type  :object,
                                                     :ident :bw-12step/A},
                                      :B
                                      #:plan.problem{:type  :object,
                                                     :ident :bw-12step/B},
                                      :C
                                      #:plan.problem{:type  :object,
                                                     :ident :bw-12step/C},
                                      :D
                                      #:plan.problem{:type  :object,
                                                     :ident :bw-12step/D},
                                      :E
                                      #:plan.problem{:type  :object,
                                                     :ident :bw-12step/E},
                                      :F
                                      #:plan.problem{:type  :object,
                                                     :ident :bw-12step/F},
                                      :G
                                      #:plan.problem{:type  :object,
                                                     :ident :bw-12step/G}},
                  :init
                          [{:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/on-table,
                            :plan.problem.predicate/args [:bw-12step/A]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/clear,
                            :plan.problem.predicate/args [:bw-12step/A]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/on-table,
                            :plan.problem.predicate/args [:bw-12step/B]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/clear,
                            :plan.problem.predicate/args [:bw-12step/B]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/on-table,
                            :plan.problem.predicate/args [:bw-12step/G]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/on,
                            :plan.problem.predicate/args
                                                         [:bw-12step/F :bw-12step/G]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/on,
                            :plan.problem.predicate/args
                                                         [:bw-12step/E :bw-12step/F]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/on,
                            :plan.problem.predicate/args
                                                         [:bw-12step/D :bw-12step/E]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/on,
                            :plan.problem.predicate/args
                                                         [:bw-12step/C :bw-12step/D]}
                           {:plan.problem/type           :predicate,
                            :plan.problem.predicate/name :blocks_world/clear,
                            :plan.problem.predicate/args [:bw-12step/C]}],
                  :goal
                          [:plan.problem/and
                           [{:plan.problem/type           :predicate,
                             :plan.problem.predicate/name :blocks_world/on,
                             :plan.problem.predicate/args
                                                          [:bw-12step/B :bw-12step/C]}
                            {:plan.problem/type           :predicate,
                             :plan.problem.predicate/name :blocks_world/on-table,
                             :plan.problem.predicate/args [:bw-12step/A]}
                            {:plan.problem/type           :predicate,
                             :plan.problem.predicate/name :blocks_world/on,
                             :plan.problem.predicate/args
                                                          [:bw-12step/F :bw-12step/A]}
                            {:plan.problem/type           :predicate,
                             :plan.problem.predicate/name :blocks_world/on,
                             :plan.problem.predicate/args
                                                          [:bw-12step/C :bw-12step/D]}]]})