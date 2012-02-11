--cpsc 449 w11
--assignment 1
--andrew helwer

import Prelude hiding (elem)

-- question 1a

min::Int->Int->Int
min a b
    | a<=b		= a
    | otherwise		= b

minThree::Int->Int->Int->Int
minThree a b c
    | a<=b&&a<=c	= a
    | b<=c		= b
    | otherwise		= c
  
-- question 2

bico::Int->Int->Int
bico n k
    | n == k		= 1
    | n == 0		= 0
    | k == 0		= 1
    | otherwise		= (bico (n-1) (k-1)) + (bico (n-1) k)
    
-- question 3

matches::Int->[Int]->[Int]
matches n x = [a | a<-x, a==n]

elem::Int->[Int]->Bool
elem n x = (matches n x) /= []

-- question 4

type Person = String
type Book = String
type Database = [(Person,[Book])]

books::Database->Person->[Book]
books db per = [b | (p, books)<-db, p==per, b<-books] 

-- You would think that adding additional restrictions on the set definition would
-- result in a subset. Not in Haskell, apparently.

borrowers::Database->Book->[Person]
borrowers db b = [p | (p, books)<-db, b<-books]

--borrowed::Database->Book->Bool
--borrowed db b = (b<-[book | (per,books)<-db, b<-books])

--numBorrowed::Database->Person->Int

--makeLoan::Database->Person->Book->Database

--returnLoad::Database->Person->Book->Database

-- question 5

-- I had a nice recursive algorithm worked out (the bones of which may be seen below),
-- but hugs decided it wouldn't have it and proceeded to spout vague 'pattern match
-- failure' errors on certain inputs.

--scale::Picture->Int->Picture
--scale pic n
  --  | n<=0		= []
   -- | 

scale::[Char]->Int->[[Char]]
scale [pixel] n = [scalechar p n | p<-[pixel]]

scalelist::[Char]->Int->[[Char]]
scalelist [pixel] n
    | n==1		= [pixel]:[]
    | otherwise		= [pixel]:(scalelist [pixel] (n-1))
    
scalechar::Char->Int->[Char]
scalechar pixel n
    | n==1		= pixel:[]
    | otherwise		= pixel:(scalechar pixel (n-1))