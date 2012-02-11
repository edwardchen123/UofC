{-
CPSC 449 Assignment 2
J. Gallagher T01 MW 1400-1450
Andrew Helwer - 10023875
-}

{-
Question 1a

Purpose: to square all odd items in a list of integers
Parameters: a list of integers
Preconditions: none
Returns: a list of integers
-}

oddSquaresA :: [Int]->[Int]
oddSquaresA xs = [x*x | x<-xs, (odd x)]

{-
Question 1b

Purpose: to square all odd items in a list of integers
Parameters: a list of integers
Preconditions: none
Returns: a list of integers
-}

oddSquaresB :: [Int]->[Int]
oddSquaresB [] = []
oddSquaresB (x:xs)
    | (odd x)			= (x*x):(oddSquaresB xs)
    | otherwise			= (oddSquaresB xs)
    
{-
Question 2 (List Comprehension)

Purpose: to remove all duplicates from a set of integers
Parameters: a list of integers
Preconditions: none
Returns: a list of unique integers
-}

uniqueA :: [Int]->[Int]
uniqueA xs = [x | x<-xs, (isUniqueIn x xs)]

{-
Question 2 (Primitive Recursion)

Purpose: to remove all duplicates from a set of integers
Parameters: a list of integers
Preconditions: none
Returns: a list of unique integers
-}

uniqueB ::[Int]->[Int]
uniqueB [] = []
uniqueB (x:xs)
    | (contains x xs)		= (uniqueB (removeAll x xs))
    | otherwise			= x:(uniqueB xs)

{-
Question 3a

Purpose: to merge two sorted lists such that the end result is sorted
Parameters: two lists of integers to be merged
Preconditions: both lists of integers are sorted in ascending order
Returns: a sorted list of integers
-}

mergeLists :: [Int]->[Int]->[Int]
mergeLists [] [] = []
mergeLists [] ys = ys
mergeLists xs [] = xs
mergeLists (x:xs) (y:ys)
    | x<=y			= x:(mergeLists xs (y:ys))
    | otherwise			= y:(mergeLists (x:xs) ys)
{-
Question 3b

Purpose: to split a list into two lists, one of even indices one of odd
Parameters: a list of integers to be split
Preconditions: none
Returns: a tuple of two integer lists whose union is the original list
-}

splitList :: [Int]->([Int],[Int])
splitList [] = ([],[])
splitList (x:xs)
    | (xs==[])			= ([x],[])
    | otherwise			= (x:(skipOne (tail xs)),(skipOne xs))

{-
Purpose: to generate a list from an input list by taking every second element
Parameters: a list of integers
Preconditions: none
Returns: a list of integers
-}

skipOne :: [Int]->[Int]
skipOne [] = []
skipOne (x:xs)
    | (xs==[])			= [x]
    | otherwise			= x:(skipOne (tail xs))

{-
Question 3c

Purpose: to sort a list of integers with mergesort
Parameters: a list of integers
Preconditions: none
Returns: a list of integers
-}

mSort :: [Int]->[Int]
mSort [] = []
mSort (x:xs)
    | (xs==[])			= [x]
    | otherwise			= (mergeLists (mSort (x:(skipOne (tail xs)))) (mSort(skipOne xs)))
    
{-
Useful functions written by myself:
-}

{-
Purpose: to check if a list of integers contains a certain element
Parameters: an element to be checked and a list of integers
Preconditions: none
Returns: True or False if the element is or is not in the list
-}

contains :: Int->[Int]->Bool
contains x xs
    = case xs of
	   [] -> False
	   (y:ys) -> (x==y) || (contains x ys)

{-
Purpose: to find the number of instances of an element in a list
Parameters: an element to be checked and a list of integers
Preconditions: none
Returns: an integer specifying number of occurences in the list
-}

instancesOf :: Int->[Int]->Int
instancesOf n [] = 0
instancesOf n (x:xs)
    | (n==x)			= 1 + (instancesOf n xs)
    | otherwise			= (instancesOf n xs)
    
{-
Purpose: to determine if there is exactly one occurence of an element in a list
Parameters: an element to be checked and a list of integers
Preconditions: none
Returns: True or False if the element is or is not unique
-}
isUniqueIn :: Int->[Int]->Bool
isUniqueIn x xs = (instancesOf x xs) == 1
    
{-
Purpose: to remove all instances of an element in a list
Parameters: an element to be removed and a list of integers
Preconditions: none
Returns: a list with all occurences of the specified element gone
-}
removeAll :: Int->[Int]->[Int]
removeAll n [] = []
removeAll n (x:xs)
    | (x==n)			= (removeAll n xs)
    | otherwise			= x:(removeAll n xs)