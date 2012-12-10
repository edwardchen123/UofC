! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Assignment 3

.global main

define(size, 20)			! Defines the size of the array
define(bytes, 4)			! Defines the number of bytes in each array index to be 4
define(variant, %l0)		! Defines %l0 as a loop variant register
define(gap, %l0)			! Defines %l0 as a loop variant register
define(i, %l1)				! Defines %l1 as a loop variant register
define(j, %l2)				! Defines %l2 as a loop variant register
define(index1, %l3)			! Defines %l3 as an index of the array
define(index2, %l4)			! Defines %l4 as an index of the array
define(elem1, %l5)			! Defines %l5 as an element of the array
define(elem2, %l6)			! Defines %l6 as an element of the array

element:	.asciz		"v[%d] = %d\n"		! String formatter for array contents output
startLabel:	.asciz		"Unsorted array:\n"	! Label for the unsorted array
			.align 4
endLabel:	.asciz		"\nSorted array:\n" ! Label for the sorted array
			.align 4

main:

	save 	%sp, -96, %sp		! Transfers value of stack pointer to frame pointer
	mov		size, %o0			! Moves size into %o0
	sll		%o0, 2, %o0			! Multiplies size by 4
	sub 	%sp, %o0, %sp		! Allocates size*4 bytes of memory for integer array
	
	set		startLabel, %o0		! Prints out label
	call	printf				! Prints
	mov 	1, variant			! Initializes loop variant to 1
	
	ba 		fillArrayTest		! Branches to fillArrayTest
	nop
	
fillArray:
	
	call 	rand				! Returns a random integer to %o0
	set 	0xff, %l7			! Sets bitmask in %l7
	and 	%o0, %l7, %o0		! Reduces the random integer modulo 256
	mov 	%o0, elem1			! elem1 = random integer reduced modulo 256
	sll		variant, 2, %o0		! variant*4
	sub 	%fp, %o0, index1	! index1 = %fp - variant*bytes
	st		elem1, [index1]		! Stores elem1 in index1
	
	set		element, %o0		! Moves finalString formatter into %o0
	sub		variant, 1, %o1		! Subtracts 1 from index1 and moves it into %o1 to be printed
	call	printf				! Prints
	mov		elem1, %o2			! Moves elem1 into %o2 to be printed
	
	inc 	variant				! Increments the variant by 1

fillArrayTest:

	cmp 	variant, size		! Compares the loop variant with the size of the array
	ble		fillArray			! Branches to fillArray if variant <= size
	mov		size, %o0			! Moves size into %o0
	
	srl		%o0, 1, gap			! gap = size/2
	ba		shellSortTest		! Branches to shellSortTest
	nop
	
shellSort:

	ba 		iterateTest			! Branches to iterateTest
	nop
	
	iterate:
	
		ba		exchangeTest			! Branches to exchangeTest
		nop
		
		exchange:
		
			st		elem1, [index2]			! v[j+gap] = v[j]
			st		elem2, [index1]			! v[j] = v[j+gap]
			
			sub		j, gap, j				! j-= gap
		
		exchangeTest:
		
			cmp		j, 0				! Compares j, 0
			bl		false				! Branches to false if j < 0
			add		j, 1, %o0				! Adds 1 to j
			sll		%o0, 2, %o0				! Multiplies j by 4
			sub 	%fp, %o0, index1		! index1 = %fp - j*bytes
			add		gap,j, %o0				! Sums gap and j
			inc		%o0						! Adds 1 to j+gap
			sll		%o0, 2, %o0				! Computes (gap+j)*bytes
			sub		%fp, %o0, index2		! index2 = %fp - (gap+j)*bytes
			ld		[index1], elem1			! elem1 = v[j]
			ld		[index2], elem2			! elem2 = v[j+gap]
			
			cmp		elem1, elem2		! Compares v[j], v[j+gap]
			bg		exchange			! Branches to exchange if v[j] < v[j+gap]
			nop
			
		false:
		
		inc		i					! i++
	
	iterateTest:
	
		cmp		i, size				! Compares i, size
		bl		iterate				! Branches to iterate if i < size
		sub		i, gap, j			! j = i - gap
		
	srl		gap, 1, gap			! gap /= 2

shellSortTest:
	
	cmp		gap, 0				! Compares gap, 0
	bg		shellSort			! Branches to shellsort if gap > 0
	mov		gap, i				! i = gap                                      
			
	set		endLabel, %o0
	call	printf
	mov		1, variant			! Initializes variant to 1    
	
	ba		printSortedTest		! Branches to printSortedTest
	nop
	
printSorted:

	sub 	%fp, %o0, index1	! index1 = %fp - variant*bytes
	ld		[index1], elem1		! elem1 = v[index1]
	
	set		element, %o0		! Moves finalString formatter into %o0
	sub		variant, 1, %o1		! Subtracts 1 from index1 and moves it into %o1 to be printed
	call	printf				! Prints
	mov		elem1, %o2			! Moves elem1 into %o2 to be printed
	
	inc		variant				! Increments variant

printSortedTest:

	cmp		variant, size		! Compares variant to size
	ble		printSorted			! Branches to printSorted if variant <= size
	sll		variant, 2, %o0		! Multiplies variant by 4                       
	
	mov		1, %g1
	ta		0
