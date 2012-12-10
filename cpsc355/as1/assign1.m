! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Program for Question Two on Assignment One
! Calculates max value of y = a0 + a1*x + a2*x^2 + a3*x^3 between a and b

.global main

xstring:		.asciz		"x value: %i "		! String formatter for x value
ystring:		.asciz		"y value: %i "		! String formatter for y value
maxstring:		.asciz		"max value: %i\n"	! String formatter for max value

.align 4

define(a, -2)		! a is the lower limit of the interval to be checked
define(b, 7)		! b is the upper limit of the interval to be checked
define(inc, 1)		! inc is the increment by which x is increased every iteration

define(a0, -5)		! a0 is the 0th order coefficient
define(a1, 28)		! a1 is the 1st order coefficient
define(a2, -18)		! a2 is the 2nd order coefficient
define(a3, 2)		! a3 is the 3rd order coefficient

define(max, %l0)	! max is the current maximum
define(x, %l1)		! x is the current value being tested
define(y, %l2)		! y is the current value of the polynomial at x

main:

	save %sp, -96, %sp
	
	ba test						! Branches program to test
	mov		a, x				! Put the starting x value in %l1
	
	loop:
	
		mov		a0, y			! Adds a0 to y, which is added to as the terms are calculated
		
		mov		x, %o0			! Put the current x value in %o0
		call	.mul			! Returns the value of a1*x to %o0
		mov		a1, %o1			! Puts a1 in %o1
		add		%o0, y, y		! Adds a1*x to y
		
		mov		x, %o0			! Put the current x value in %o0
		call	.mul			! Returns the value of x^2 to %o0
		mov		x, %o1			! Put the current x value in %o1
		mov		%o0, %l3		! Moves x^2 to %l3 to save for later calculations
		call	.mul			! Returns the value of -18x^2 to %o0
		mov		a2, %o1			! Puts a2 in %o1
		add		%o0, y, y		! Adds a2*x^2 to y
		
		mov		%l3, %o0		! Puts the current x^2 value in %o0
		call	.mul			! Returns the value of x^3 to %o0
		mov		x, %o1			! Puts the current x value in %o1
		call	.mul			! Returns the value of a3*x^3 to %o0
		mov		a3, %o1			! Puts a3 in %o1
		add		%o0, y, y		! Adds a3*x^3 to y
		
		mov		a, %o0			! Moves a into %o0 in preparation for checking if it is equal to x value
		cmp		%o0, x			! Checks if this is the first iteration of the loop
		bne		notfirst		! Skips to notfirst if this is not the first iteration
		nop
		
		mov		y, max			! Replaces max with y value
		
	notfirst:
		
		cmp		y, max			! Compares recently calculated y with max
		ble		noupdate		! Skips to noupdate if max is greater than y
		nop
	
		mov		y, max			! Replaces max with y value
	
	noupdate:
		
		set		xstring, %o0	! Uses xstring formatter
		call	printf			! Prints out current x value
		mov		x, %o1			! Moves current x value to %o1
		
		set		ystring, %o0	! Uses ystring formatter
		call	printf			! Prints out current y value
		mov		y, %o1			! Moves current y value to %o1
		
		set		maxstring, %o0	! Uses maxstring formatter
		call	printf			! Prints out current maximum
		mov		max, %o1		! Moves current maximum to %o1
		
		add		inc, x, x		! Increments x value by one
		
	test:
	
		cmp		x, b			! Compares current x value to see if it is <= b
		ble		loop			! If x is <= b, loop again
		nop
		
	gdbbreakpoint:				! Label for convenience of putting a breakpoint in for gdb
		
		nop
	
	mov		1, %g1
	ta		0
