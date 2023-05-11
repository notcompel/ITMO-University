(load-file "proto.clj")

(defn variable [name]
  (fn [values] (get values name)))

(defn constant [x] (constantly x))

(defn operation [fun]
  (fn [& args] (fn [nums] (apply fun (mapv #(% nums) args)))))

(def add (operation + ))
(def subtract (operation -))
(def divide (operation (fn [& nums] (cond (and (<= (count nums) 1) (zero? (first nums))) ##Inf
                                          (<= (count nums) 1) (apply / nums)
                                          :else (/ (double (first nums)) (double (apply * (drop 1 nums))))
                                          ))))
(def multiply (operation *))
(defn mean-eval [& args] (/ (apply + args) (count args)))
(def mean (operation mean-eval))
(defn varn-eval [& args] (-
                           (/
                             (apply + (mapv #(Math/pow % 2) args))
                             (count args))
                           (Math/pow (/ (apply + args) (count args)) 2)
                           ))
(def varn (operation varn-eval))

(defn negate [arg] (fn [nums] (- (arg nums))))
(def ops {:+ add
          :- subtract
          :/ divide
          :* multiply
          :negate negate
          :mean mean
          :varn varn
          }
  )



(def _name (field :name))
(def _const (field :const))
(def _eval (field :eval))
(def _str (field :str))
(def _diffEval (field :diffEval))
(def _args (field :args))
(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))

(declare ZERO)
(defn ConstrConstant [this const]
  (assoc this :const const))
(def ConstantProto {
                    :evaluate (fn [this _] (_const this))
                    :toString (fn [this] (str (_const this)))
                    :diff (fn [_ _] ZERO)
                    })

(def Constant (constructor ConstrConstant ConstantProto))
(def ZERO (Constant 0.0))
(def ONE (Constant 1.0))
(def TWO (Constant 2.0))
(def VariableProto {
                    :evaluate (fn [this values] (get values (_name this)))
                    :toString (fn [this] (str (_name this)))
                    :diff     (fn [this dname] (cond (= (_name this) dname) ONE
                                                     :else ZERO))})

(defn ConstrVariable [this name]
  (assoc this :name name))

(def Variable (constructor ConstrVariable VariableProto))


(def CreateOperationProto {
                           :evaluate (fn [this vals] (apply (_eval this) (mapv #(evaluate % vals) (_args this))))
                           :toString #(str "(" (_str %) " " (clojure.string/join " " (mapv toString (_args %))) ")")
                           :diff     (fn [this dname]
                                       ((_diffEval this)
                                        (_args this)
                                        (mapv #(diff % dname)
                                              (_args this))))})


(defn ConstrCreateOperation [this eval str diff-eval]
  (let [ConstrAbstractOperation (fn [this & args] (assoc this :args args))
        AbstractOperationProto (assoc CreateOperationProto :eval eval :str str :diffEval diff-eval)
        AbstractOperation (constructor ConstrAbstractOperation AbstractOperationProto)]
    (fn [& args] (apply AbstractOperation args))))


(def CreateOperation (constructor ConstrCreateOperation CreateOperationProto))

(def Add (CreateOperation + "+" (fn [args dargs] (apply Add dargs))))
(def Subtract (CreateOperation - "-" (fn [args dargs] (apply Subtract dargs))))
(def Multiply (CreateOperation * "*" (fn [args dargs] (let [n (count args)]
                                                        (cond (== n 1) (first dargs)
                                                              :else (second (reduce
                                                                              (fn [[expr1 dexpr1] [expr2 dexpr2]]
                                                                                [(Multiply expr1 expr2)
                                                                                 (Add (Multiply expr2 dexpr1)
                                                                                      (Multiply expr1 dexpr2))])
                                                                              (map vector args dargs))))))))

(def Negate (CreateOperation - "negate" (fn [_ dargs] (apply Negate dargs))))
(def Divide (CreateOperation (fn [& nums] (cond (and (<= (count nums) 1) (zero? (first nums))) ##Inf
                                                (<= (count nums) 1) (apply / nums)
                                                :else (/ (double (first nums)) (double (apply * (drop 1 nums))))
                                                )) "/"
                             (fn [args dargs] (let [n (count args)]
                                                (cond (== n 1) (Multiply (Negate (first dargs)) (Divide (Multiply (first args) (first args))))
                                                      :else (second (reduce
                                                                      (fn [[expr1 dexpr1] [expr2 dexpr2]]
                                                                        [(Divide expr1 expr2)
                                                                         (Divide (Subtract (Multiply dexpr1 expr2)
                                                                                           (Multiply expr1 dexpr2))
                                                                                 (Multiply expr2 expr2)
                                                                                 )])
                                                                      (map vector args dargs))))))))

(def Mean (CreateOperation mean-eval "mean" (fn [args dargs] (apply Mean dargs))))

(def Varn (CreateOperation varn-eval "varn" (fn [args dargs] (let [sums (mapv (fn [a b] (Multiply a b)) args dargs)]
                                                               (Subtract (Multiply TWO (apply Mean sums))
                                                                         (Multiply TWO (Multiply (apply Mean args) (apply Mean dargs))))))))


(def ops-obj {:+ Add
              :- Subtract
              :/ Divide
              :* Multiply
              :negate Negate
              :mean Mean
              :varn Varn
              }
  )

(def ops-func {:+ add
               :- subtract
               :/ divide
               :* multiply
               :negate negate
               :mean mean
               :varn varn
               }
  )

(defn myParseObject [expr]
  (cond (list? expr) (apply (get ops-obj (keyword (first expr))) (mapv myParseObject (drop 1 expr)))
        (number? expr) (Constant expr)
        :else (Variable (name expr)))
  )

(defn parseObject [input]
  (myParseObject (read-string input)))

(defn myParseFunction [expr]
  (cond (list? expr) (apply (get ops-func (keyword (first expr))) (mapv myParseFunction (drop 1 expr)))
        (number? expr) (constant expr)
        :else (variable (name expr)))
  )

(defn parseFunction [input]
  (myParseFunction (read-string input)))