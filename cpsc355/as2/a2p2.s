! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Program Part B on Assignment Two

.global main

! %l0 - product
! %l1 - multiplicand
! %l2 - multiplier
! %l3 - bit counter
! %l4 - round counter
! %l5 - stores whether multiplier is negative

initProduct:	.asciz		"Initial product value: %x\n"
				.align 4

finalProduct:	.asciz		"Final product value: %x\n"
				.align 4
				
initMlier:		.asciz		"Initial multiplier value: %x\n"
				.align 4

finalMlier:		.asciz		"Final multiplier value: %x\n"
				.align 4

initMcand:		.asciz		"Initial multiplicand value: %x\n"
				.align 4

finalMcand:		.asciz		"Final multiplicand value: %x\n"
				.align 4

main:
	
	save %sp, -96, %sp
	
	mov		0, %l3				! Initializes bit counter
	mov		1, %l4				! Initializes round counter
	mov		0, %l0				! Initializes product
	
	set		0x04ee67b7, %l1		! Sets multiplicand for round 1
	set		0x072e8b8c, %l2		! Sets multiplier for round 1
	
	set		initProduct, %o0	! Moves initProduct formatter into %o0
	call	printf				! Prints
	mov		%l0, %o1			! Moves product into %o1
	
	set		initMlier, %o0		! Moves initMlier formatter into %o0
	call	printf				! Prints
	mov		%l2, %o1			! Moves product into %o1
	
	set		initMcand, %o0		! Moves initMcand formatter into %o0
	call	printf				! Prints
	mov		%l1, %o1			! Moves product into %o1
	
	srl		%l2, 31, %l5		! Determines if multiplier is negative
		
	ba 		loopTest			! Enters loop
	nop
	
loop:
	
	and		%l2, 1, %o0			! Isolates rightmost bit in multiplier
	cmp		%o0, 1				! Checks if rightmost bit in multiplier is 1
	bne		noAdd				! Skips to noAdd if rightmost bit in multiplier is not 1
	nop
	
	add		%l0, %l1, %l0		! product += multiplicand
	
noAdd:
	
	and		%l0, 1, %o0			! Isolates rightmost bit of product
	sll		%o0, 31, %o0		! Moves rightmost bit of product to leftmost bit
	srl		%l2, 1, %l2			! Shift multiplier to right by 1
	or		%o0, %l2, %l2		! Moves rightmost bit of product onto leftmost bit of multiplier
	sra		%l0, 1, %l0			! Shift product to right by 1
	
	add		%l3, 1, %l3			! Increments counter
	
loopTest:
	
	cmp		%l3, 32				! Checks if counter is < 32
	bl		loop				! Branches to loop if counter is < 32
	nop
	
	cmp		%l5, 1				! Checks if multiplier is negative
	bne		nonNegative			! Branches if multiplier is non-negative
	nop
	
	sub		%l0, %l1, %l0		! Subtracts multiplicand from product
	
nonNegative:

	set		finalProduct, %o0	! Moves finalProduct formatter into %o0
	call	printf				! Prints
	mov		%l0, %o1			! Moves product into %o1
	
	set		finalMlier, %o0		! Moves finalMlier formatter into %o0
	call	printf				! Prints
	mov		%l2, %o1			! Moves product into %o1
	
	set		finalMcand, %o0		! Moves finalMcand formatter into %o0
	call	printf				! Prints
	mov		%l1, %o1			! Moves product into %o1
	
	add		%l4, 1, %l4			! Increments round counter
	mov		0, %l0				! Reinitializes product
	mov		0, %l3				! Reinitializes bit counter
	
	cmp		%l4, 2				! Checks if round counter = 2
	be		roundTwo			! If so, branches to roundTwo
	nop
	
	cmp		%l4, 3				! Checks if round counter = 3
	be		roundThree			! If so, branches to roundThree
	nop
	
	ba		endProgram			! If counter is not at 2 or 3, branches to endProgram
	nop
	
roundTwo:

	set		0x04ee67b7, %l1		! Sets multiplicand for round 2
	set		0xf8d17474, %l2		! Sets multiplier for round 2
	
	set		initProduct, %o0	! Moves initProduct formatter into %o0
	call	printf				! Prints
	mov		%l0, %o1			! Moves product into %o1
	
	set		initMlier, %o0		! Moves initMlier formatter into %o0
	call	printf				! Prints
	mov		%l2, %o1			! Moves product into %o1
	
	set		initMcand, %o0		! Moves initMcand formatter into %o0
	call	printf				! Prints
	mov		%l1, %o1			! Moves product into %o1
	srl		%l2, 31, %l5		! Determines if multiplier is negative
	
	ba 		loopTest			! Enters loop
	nop
	
roundThree:

	set		0xfb119849, %l1		! Sets multiplicand for round 3
	set		0xf8d17474, %l2		! Sets multiplier for round 3
	
	set		initProduct, %o0	! Moves initProduct formatter into %o0
	call	printf				! Prints
	mov		%l0, %o1			! Moves product into %o1
	
	set		initMlier, %o0		! Moves initMlier formatter into %o0
	call	printf				! Prints
	mov		%l2, %o1			! Moves product into %o1
	
	set		initMcand, %o0		! Moves initMcand formatter into %o0
	call	printf				! Prints
	mov		%l1, %o1			! Moves product into %o1
	srl		%l2, 31, %l5		! Determines if multiplier is negative
	
	ba 		loopTest			! Enters loop
	nop
	
endProgram:
	
	mov		1, %g1
	ta		0
