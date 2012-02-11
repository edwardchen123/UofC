{-
CPSC 449 Assignment 4
J. Gallagher T01 MW 1400-1450
Andrew Helwer - 10023875
-}
{-
This is an algebraic data type which may be used to represent a well-formed formula of Propositional Logic.
-}
data Formula = 	FSym Int |
		FNot Formula |
		FAnd Formula Formula |
		FOr Formula Formula

{-
TruthAssignment is a type that is of type Int->Bool. It is intended to map the FSym constructor of Formula to a boolean value.
-}
type TruthAssignment = 	Int -> Bool

{-
Question 1a
Name: s
Purpose: To define a mapping from an integer to a boolean value. This is not a total function. s is itself of type TruthAssignent, so may be passed to any function that takes type TruthAssignment as a parameter.
Parameters: An integer
Preconditions: The integer is either 0, 1, or 10 - otherwise it will not be defined.
Returns: A boolean value based on the value of the input.
-}
s :: Int -> Bool
s 0	= True
s 1	= False
s 10	= True

{-
Question 1b
Name: g
Purpose: To evaluate the formula (FOr (FSym 1) (FNot (FSym 0))). g is the denotation of this formula.
Parameters: A function of type TruthAssignment.
Preconditions: TruthAssignment is defined on all values of the FSym constructors in the formula.
Returns: A boolean value which is the evaluation of the formula using the given TruthAssignment.
-}
g :: TruthAssignment -> Bool
g ta	= (ta 1)||(not (ta 0))

{-
Question 1c
Name: compileFormula
Purpose: To take a Formula and return a denotation of that formula, such that if a TruthAssignment is specified for the denotation the formula it will be evaluated in a correct fashion.
Parameters: A valid Formula.
Preconditions: None
Returns: A denotation of the given Formula, which is a function of type TruthAssignment->Bool.
-}
compileFormula :: Formula -> (TruthAssignment -> Bool)
compileFormula (FSym a)		= (\x->(x a))
compileFormula (FNot f)		= \x->(not (compileFormula f x))
compileFormula (FAnd f1 f2)	= \x->(compileFormula f1 x)&&(compileFormula f2 x)
compileFormula (FOr f1 f2)	= \x->(compileFormula f1 x)||(compileFormula f2 x)

{-
Question 3
The following is largely composed of code given to us for this assignment in the file parser.hs. I have implemented the following functions: isOp, charToOp, makeExpr, and stringToExpr. optional and neList I was unable to implement.
-}

-- Parser Type

type Parse a b = [a] -> [(b,[a])]

-- Basic Parsers

none :: Parse a b
none inp = []

succeed :: b -> Parse a b
succeed val inp = [(val,inp)]

-- Basic Parser Builders

token :: Char -> Parse Char Char
token t [] = []
token t (x:xs) 
 | t == x    = [(t,xs)]
 | otherwise = []

spot :: (a->Bool) -> Parse a a
spot p [] = []
spot p (x:xs)
 | p x       = [(x,xs)]
 | otherwise = []

-- Parser Combinators

alt :: Parse a b -> Parse a b -> Parse a b
alt p1 p2 inp = (p1 inp) ++ (p2 inp)

sqn :: Parse a b -> Parse a c -> Parse a (b,c)
sqn p1 p2 inp
 = [((v1,v2),rem2) | (v1,rem1) <- p1 inp,
                     (v2,rem2) <- p2 rem1 ]

build :: Parse a b -> (b->c) -> Parse a c
build p f inp = [ (f x, rem) | (x,rem) <- p inp ]

list :: Parse a b -> Parse a [b]
list p = alt (succeed [])
             (build (sqn p (list p))
                    (uncurry (:)))

-- Expressions

data Expr = Lit Int |
            Var Char |
            Op Ops Expr Expr
data Ops = Add | Sub | Mul | Div | Mod

showExpr :: Expr -> String
showExpr (Lit n)       
 | n < 0               = "~" ++ (show (-n))
 | otherwise           = show n
showExpr (Var ch)      = [ch]
showExpr (Op op e1 e2) = "(" ++ (showExpr e1) ++ (showOps op)
                             ++ (showExpr e2) ++ ")"

showOps :: Ops -> String
showOps Add = "+"
showOps Sub = "-"
showOps Mul = "*"
showOps Div = "/"
showOps Mod = "%"

-- Expression Parser (Top-Level)

parser :: Parse Char Expr
parser = alt varParse
             (alt opExpParse
                  litParse)

showParse :: [(Expr,[Char])] -> String
showParse [] = ""
showParse [x] = showParse1 x
showParse (x:xs) = (showParse1 x) ++ ", " ++ (showParse xs)

showParse1 :: (Expr,[Char]) -> String
showParse1 (e,rem) = "(" ++ (showExpr e) ++ ", \"" ++ rem ++ "\")"

-- Variable Parser

varParse :: Parse Char Expr
varParse = build (spot isVar) Var

isVar :: Char -> Bool
isVar x = ('a' <= x && x <= 'z')

-- Operator Expression Parser

opExpParse :: Parse Char Expr
opExpParse = build (sqn (token '(')
                        (sqn parser
                             (sqn (spot isOp)
                                  (sqn parser
                                       (token ')')))))
                   makeExpr

-- Literal Parser

litParse :: Parse Char Expr
litParse
  = build (sqn (optional (token '~'))
               (neList (spot isDigit)))
          (stringToExpr . uncurry (++))

-- Your code goes here ...

{-
Name: isOp
Purpose: To determine whether a char is a valid representation of an operator.
Parameters: A char to be tested.
Prerequisites: Ops are defined using the chars '+','-','*','/', and '%'.
Returns: A boolean value based on whether the char is a valid operator.
-}
isOp :: Char -> Bool
isOp x = (elem x ['+','-','*','/','%'])

{-
Name: charToOp
Purpose: Returns the operator associated with a specific character.
Parameters: A char to be converted into Ops.
Prerequisites: The character is a valid representation of an operator.
Returns: The operator associated with the character.
-}
--charToOp :: Char -> Maybe Ops
charToOp x
	| x=='+'	= Add
	| x=='-'	= Sub
	| x=='*'	= Mul
	| x=='/'	= Div
	| x=='%'	= Mod

{-
Name: makeExpr
Purpose: Converts two expressions joined with an operator into proper form.
Parameters: A nested pair.
Prerequisites: The string has been parsed using opExpParse.
Returns: A properly-defined Expr of the form Op (Ops) Expr Expr.
-}
makeExpr :: (a,(Expr,(Char,(Expr,b)))) -> Expr
makeExpr (_,(e1,(ch,(e2,_)))) = Op (charToOp ch) e1 e2

{-
Unfortunately I was not able to get either optional or neList working.
-}
optional :: Parse a b -> Parse a [b]
optional p = alt (succeed []) (build (sqn p (optional p)) (uncurry (:)))

neList :: Parse a b -> Parse a [b]
neList p = alt (succeed []) (build (sqn p (neList p)) (uncurry (:)))

{-
Name: stringToExpr
Purpose: To convert a string representation of an integer into an integer
Parameters: A string
Prerequisites: The string is composed of ASCII digits optionally prepended with ~.
Returns: The integer representation of the string.
-}
stringToExpr :: String -> Expr
stringToExpr (x:xs)
	| (x=='~')	= Lit ((read xs)*(-1))
	| otherwise	= Lit (read (x:xs))

isDigit :: Char -> Bool
isDigit x = ('0' <= x && x <= '9')

{-
Question 4
Name: Pairs
Purpose: To generate an infinite list of pairs of positive integers.
Parameters: None
Prerequisite: elem (m,n) pairs returns True in finite time
Returns: An infinite list of pairs (m,n) such that m and n are positive integers and m <= n.
-}
pairs :: [(Int,Int)]
pairs = [(x,y) | x <- [0..], y <- [0..x]] 

{-
Question 5
Names: factorial and fibbonacci
Purpose: To generate an infinite list of the factorial and fibbonacci numbers, respectively.
-}
factorial :: [Int]
factorial = 1:[x*(factorial!!(x-1))|x<-[1..]]

fibonacci :: [Int]
fibonacci = 1:1:[(fibonacci!!(x-2))+(fibonacci!!(x-1))|x<-[2..]]

