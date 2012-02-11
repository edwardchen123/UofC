! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Program for Question One on Assignment One
! Calculates max value of y = 2x^3 - 18x^2 + 28x - 5 between -2 and 7

.global main

xstring:		.asciz		"x value: %i "		! String formatter for x value
ystring:		.asciz		"y value: %i "		! String formatter for y value
maxstring:		.asciz		"max value: %i\n"	! String formatter for max value

.align 4

main:

	save %sp, -96, %sp
	
	mov		-2, %l1				! Put the starting x value (-2) in %l1
	ba test						! Branches program to test
	nop
	
	loop:
	
		mov		-5, %l2			! Puts -5 in %l2, which is added to as the terms are calculated
		
		mov		%l1, %o0		! Put the current x value in %o0
		mov		28, %o1			! Puts 28 in %o1
		call	.mul			! Returns the value of 28x to %o0
		nop
		add		%o0, %l2, %l2	! Adds 28x to %l2
		
		mov		%l1, %o0		! Put the current x value in %o0
		mov		%l1, %o1		! Put the current x value in %o1
		call	.mul			! Returns the value of x^2 to %o0
		nop
		mov		%o0, %l3		! Moves x^2 to %l3 to save for later calculations
		mov		-18, %o1		! Puts -18 in %o1
		call	.mul			! Returns the value of -18x^2 to %o0
		nop
		add		%o0, %l2, %l2	! Adds -18x^2 to 28x - 5
		
		mov		%l3, %o0		! Puts the current x^2 value in %o0
		mov		%l1, %o1		! Puts the current x value in %o1
		call	.mul			! Returns the value of x^3 to %o0
		nop
		mov		2, %o1			! Puts 2 in %o1
		call	.mul			! Returns the value of 2x^3 to %o0
		nop
		add		%o0, %l2, %l2	! Adds 2x^3 to -18x^2 + 28x - 5
		
		mov		-2, %o0			! Moves -2 into %o0 in preparation for checking if it is equal to x value
		cmp		%o0, %l1		! Checks if this is the first iteration of the loop
		bne		notfirst		! Skips to notfirst if this is not the first iteration
		nop
		
		mov		%l2, %l0		! Replaces max with y value
		
	notfirst:
	
		cmp		%l2, %l0		! Compares recently calculated y with max
		ble		noupdate		! Skips to noupdate if max is greater than y
		nop
	
		mov		%l2, %l0		! Replaces max with y value
	
	noupdate:
		
		set		xstring, %o0	! Uses xstring formatter
		mov		%l1, %o1		! Moves current x value to %o1
		call	printf			! Prints out current x value
		nop
		
		set		ystring, %o0	! Uses ystring formatter
		mov		%l2, %o1		! Moves current y value to %o1
		call	printf			! Prints out current y value
		nop
		
		set		maxstring, %o0	! Uses maxstring formatter
		mov		%l0, %o1		! Moves current maximum to %o1
		call	printf			! Prints out current maximum
		nop
		
		add		1, %l1, %l1		! Increments x value by one
		
	test:
	
		cmp		%l1, 7			! Compares current x value to see if it is <= 7
		ble		loop			! If x is <= 7, loop again
		nop
	
	mov		1, %g1
	ta		0
