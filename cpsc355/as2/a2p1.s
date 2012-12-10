! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Program Part A on Assignment Two

.global main

! %l0 - hash
! %l1 - bit index counter
! %l2 - Determines which register to copy into %l3
! %l3 - hashing register
! %l4/5/6/7 - input registers

initString:		.asciz		"Initial checksum: 0x%x\n"		! String formatter for hex output
				.align 4

inputString:	.asciz		"Input register: 0x%x\n"			! String formatter for input
				.align 4

finalString:	.asciz		"Final checksum: 0x%x\n"		! String formatter for hex output
				.align 4

main:

	save %sp, -96, %sp
	
	! Sets input registers
	set		0xaaaa8c01, %l4	! Sets input for %l4
	set		0xff001234, %l5	! Sets input for %l5
	set		0x13579bdf,	%l6	! Sets input for %l6
	set		0xc8b4ae32, %l7	! Sets input for %l7
	set		0xffffffff, %l0	! Fills %l0 with 1's
	
	set		0x0000ffff, %o0		! Moves bitmask into %o0
	and		%l0, %o0, %l0		! Turns all bits past index 15 into 0s in %l0
	set		initString, %o0		! Moves initString formatter into %o0
	call	printf				! Prints
	mov		%l0, %o1			! Moves %l0 into %o1 to be printed
	
	set		inputString, %o0	! Moves inputString formatter into %o0
	call	printf				! Prints
	mov		%l4, %o1			! Moves input into %o1 to be printed
	
	set		inputString, %o0	! Moves inputString formatter into %o0
	call	printf				! Prints
	mov		%l5, %o1			! Moves input into %o1 to be printed
	
	set		inputString, %o0	! Moves inputString formatter into %o0
	call	printf				! Prints
	mov		%l6, %o1			! Moves input into %o1 to be printed
	
	set		inputString, %o0	! Moves inputString formatter into %o0
	call	printf				! Prints
	mov		%l7, %o1			! Moves input into %o1 to be printed
	
	mov		32, %l1			! Sets bit index counter to 32
	mov		%l4, %l3		! Copies %l4 into hashing register
	ba		hashTest		! Branches to hashTest
	mov		4, %l2			! Specified current register to be hashed
	
	hash:
		
		! Places bit from hashing register into 0th index of %l0
		sll		%l0, 1, %l0			! Moves %l0 to left by one bit
		srl		%l3, 31, %o0		! Moves current bit into %o0
		or		%l0, %o0, %l0		! Moves current bit into 0th spot in %l0
		
		! Checks if xor is necessary
		set		0x00010000, %o0		! Moves bitmask into %o0
		and		%l0, %o0, %o1		! Isolates bit of index 16 in %l0
		cmp		%o0, %o1			! Checks if bit is 1 or not
		bne		noxor				! Branches if no xor is necesssary (bit = 0)
		sll		%l3, 1, %l3			! Shift hashing register to left by 1 bit
		
		set		0x00001021, %o0		! Moves xor bitmask into %o0
		xor		%l0, %o0, %l0		! xors bitmask with %l0
		
	noxor:
		
		sub		%l1, 1, %l1			! Decreases current bit index by 1
		
	hashTest:
	
		cmp 	%l1, 0				! Compares current bit index to 0
		bg		hash				! Branches if %l1 is > 0
		nop
		
		mov		32, %l1				! Resets bit index counter
		add		%l2, 1, %l2			! Increments register to be hashed by 1
		
		cmp		%l2, 5				! Determines if %l5 should be hashed next
		be		hashTest			! If so, branches to hashFive
		mov		%l5, %l3			! Moves %l5 into hashing register		
		
		cmp		%l2, 6				! Determines if %l6 should be hashed next
		be		hashTest				! If so, branches to hashSix
		mov		%l6, %l3			! Moves %l6 into hashing register
		
		cmp		%l2, 7				! Determines if %l7 should be hashed next
		be		hashTest			! If so, branches to hashSeven
		mov		%l7, %l3			! Moves %l7 into hashing register
		
		ba		output				! If all registers have been hashed, branch to output
		nop 

		
	output:
	
		set		0x0000ffff, %o0		! Moves bitmask into %o0
		and		%l0, %o0, %l0		! Turns all bits past index 15 into 0s in %l0
		set		finalString, %o0	! Moves finalString formatter into %o0
		call	printf				! Prints
		mov		%l0, %o1			! Moves %l0 into %o1 to be printed
		
	mov		1, %g1
	ta		0
