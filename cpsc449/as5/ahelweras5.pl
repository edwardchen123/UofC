% CPSC 449 Assignment 5
% Andrew Helwer - 10023875
% J. Gallagher T01 MW 1400-1450

% Question 1
/* substitute(X,Y,L1,L2) is true if and only if every occurence of X in L1 has been replaced with Y to form L2. The algorithm considers the two main cases - either the head of L1 is an occurrence of X and has been changed to Y, or it is not an occurence of X and substitute is true when called on the tails.*/

substitute(X,Y,[],[]).
substitute(X,Y,[X|T1],[Y|T2]) :- substitute(X,Y,T1,T2).
substitute(X,Y,[H|T1],[H|T2]) :- X\=H, substitute(X,Y,T1,T2).

% Question 2
/* my_select(X, L1, L2) is true if and only if inserting X at some index of L2 forms L1. The algorithm considers the two main cases - either the head of L1 is identical to X and the tail of L1 is identical to L2, or the heads of L1 and L2 are identical and  my_select is true when called on the tails.*/

my_select(X, [X|T], T).
my_select(X, [H|T1], [H|T2]) :- my_select(X,T1,T2).

% Question 3

/* The house predicate has five variables - Color, Nationality, Animal, Beverage, and Cigarettes. By convention, the country name is listed under nationality, and nothing is pluralized. */

% (a) The Englishman lives in the red house.
house(red, england, Animal, Beverage, Cigarettes) :- 
	(Animal\=dog), /* By (b), the spaniard owns the dog. */
	(Animal\=horse), /* The horse lives in the blue house. */
	(Beverage\=coffee), /* By (c), coffee is drunk in the green house. */
	(Beverate\=tea), /* By (d), the ukrainian drinks tea. */
	(Cigarettes\=kool), /* By (g), kools are smoked in the yellow house. */
	(Cigarettes\=parliament). /* By (m), the japanese man smokes parliaments. */
% (b) The Spaniard owns the dog.
house(Color, spain, dog, Beverage, Cigarettes).
% (c) Coffee is drunk in the green house.
house(green, Nationality, Animal, coffee, Cigarettes).
% (d) The Ukrainian drinks tea.
house(Color, ukraine, Animal, tea, Cigarettes).
% (f) The winston smoker owns snails.
house(Color, Nationality, snail, Beverage, winston).
% (l) The Lucky Strike smoker drinks orange juice.
house(Color, Nationality, Animal, orangejuice, luckystrike).
% (m) The Japanese smokes Parliaments.
house(Color, japan, Animal, Beverage, parliament).

house(yellow, norway, Animal, Beverage, kool) :- 
	(Animal\=dog), /* By (b), the spaniard owns the dog. */
	(Animal\=horse), /* By (k), the horse is kept in H2, not H1. */
	(Animal\=snail), /* By (j), the winston smoker owns snails. */
	(Beverage\=coffee), /* By (c), coffee is drunk in the green house. */
	(Beverage\=tea), /* By (d), the ukrainian drinks tea. */
	(Beverage\=milk), /* By (h), milk is drunk in the middle house. */
	(Beverage\=orangejuice). /* By (l), the lucky strike smoker drinks orange juice. */
house(blue, Nationality, horse, Beverage, Cigarettes) :-
	(Nationality\=spain), /* By (b), the spaniard owns a dog. */
	(Nationality\=england), /* By (a), the englishman lives in the red house. */
	(Nationality\=norway), /* By (n), the norwegian lives next to this house. */
	(Beverage\=coffee). /* By (c), coffee is drunk in the green house. */

% Question 4a

/* expr(X) checks if X is a valid expression as defined in the given context-free grammar. number(X) checks if X is a valid number. */

expr(lit(X)) :- number(X).
expr(add(E1,E2)) :- expr(E1), expr(E2).
expr(sub(E1,E2)) :- expr(E1), expr(E2).

% Question 4b

/* assoc(E1,E2) checks if E2 is the right-associative form of E1. */

% base cases:
assoc(add(lit(X),lit(Y)),add(lit(X),lit(Y))).
assoc(sub(lit(X),lit(Y)),sub(lit(X),lit(Y))).

% general cases:
