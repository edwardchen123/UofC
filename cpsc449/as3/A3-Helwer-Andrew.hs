{-
CPSC 449 Assignment 3
J. Gallagher T01 MW 1400-1450
Andrew Helwer - 10023875
-}

{-
Question 1a
Declare an algebraic type Formula to represent wffs
-}
    
data Formula	= Symb Char |
		Not Formula |
		And Formula Formula |
		Or Formula Formula

{-
Question 1c
Define a function that returns a string representation of a Formula

Name: showFormula
Purpose: to return a string representation of a formula
Parameters: an expression of type Formula as defined above
Preconditions: the expression is a valid expression of type Formula
Returns: a string representation of the Formula
-}

showFormula :: Formula -> String
showFormula (Symb a)	= [a]
showFormula (Not f)	= "~"++(showFormula f)
showFormula (And f1 f2)	= "("++(showFormula f1)++"^"++(showFormula f2)++")"
showFormula (Or f1 f2)	= "("++(showFormula f1)++"v"++(showFormula f2)++")"

-- Make Formula an instance of Show class to easily print output:
instance Show Formula where
    show f = (showFormula f)

{-
Question 1d
Define a formula that removes all disjunctions using De Morgan's law

Name: rewrite
Purpose: to remove all disjunctions from a Formula without altering meaning
Parameters: an expression of type Formula as defined above
Preconditions: the expression is a valid expression of type Formula
Returns: a Formula logically equivalent to input without any disjunctions
-}

rewrite :: Formula -> Formula
rewrite (Symb a)	= (Symb a)
rewrite (Not f)		= (Not (rewrite f))
rewrite (And f1 f2)	= (And (rewrite f1) (rewrite f2))
rewrite (Or f1 f2)	= (Not (And (Not (rewrite f1)) (Not (rewrite f2))))

{-
Question 3a

Name: or
Purpose: to determine whether at least one element of the list is True
Parameters: a list of Bools
Preconditions: the input is a valid list of Bools
Returns: True if at least one element of the list is True, False otherwise
-}

or :: [Bool] -> Bool
or xs = foldr (||) False xs

{-
Question 3b

Name: count
Purpose: to determine the number of occurences of an Int in a list of Ints
Parameters: an Int to be checked and a list of Ints
Preconditions: the input is a valid list of Ints and an Int
Returns: an Int representing the number of occurences of the element in the list
-}

count :: Int -> [Int] -> Int
count x ys = length (filter (==x) ys)

{-
Question 4a

Name: iter
Purpose: to apply f n times to some input x
Parameters: an Int representing number of composites, a function, and an input
Preconditions: the function is of type (a->a) and the input is of type a
Returns: output of type a resulting from applying f to x n times

The type of iter will be a, where a is the type of the input to the function,
which has type (a->a).
-}

iter :: Int->(a->a)->a->a
iter n f x = (foldr (.) id (replicate n f)) x

{-
Question 4b

Name: double
Purpose: given some integer, double multiplies it by two
Parameters: some Int
Preconditions: none
Returns: double the input
-}

double :: Int -> Int
double x = 2*x

{-
Name: twoToThePowerOf
Purpose: to calculate the nth power of two
Parameters: some Int
Preconditions: input is greater than or equal to 0
Returns: two raised to the power of the input
-}

twoToThePowerOf :: Int -> Int
twoToThePowerOf n = (iter n (double) 1)
