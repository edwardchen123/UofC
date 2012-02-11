! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Assignment 4

.global main

startlabel:	.asciz		"\nInitial sphere values:\n"
		.align 4

endlabel:	.asciz		"\nChanged sphere values:\n"
		.align 4

sphereprint:	.asciz		"Sphere %s origin = (%d, %d, %d)   radius = %d\n"
		.align 4

.align 4
.global name
name:	.word	firstn, secondn

firstn:		.asciz		"first"
		.align 4
secondn:	.asciz		"second"
		.align 4

define(FALSE, 0)
define(TRUE, 1)
define(x, 0)
define(y, 4)
define(z, 8)
define(r, 4)

newSphere:
	
	! Creates a new struct of specified values and returns a pointer to it
	save	%sp, -116, %sp		! Allocates memory for local variables and a struct
	sub	%fp, 20, %o0		! Creates pointer to struct then initializes variables
	sub	%fp, 12, %o1
	st	%o1, [%o0]		! Creates pointer to Point struct
	st	%g0, [%o1+x]
	st	%g0, [%o1+y]
	st	%g0, [%o1+z]
	mov	1, %o1
	st	%o1, [%o0+r]
	st	%o0, [%fp+64]		! This is the 's' variable that is returned
	ret
	restore

move:
	
	! Adds specified values to each of the pointed-to's structs variables
	save	%sp, -96, %sp
	ld	[%fp+64], %o0		! Gets pointer to struct
	ld	[%o0], %o0		! Gets pointer to point
	ld	[%o0 + x], %o1		! Loads values
	ld	[%o0 + y], %o2
	ld	[%o0 + z], %o3
	add	%o1, %i0, %o1		! Changes values
	add	%o2, %i1, %o2
	add	%o3, %i2, %o3
	st	%o1, [%o0 + x]		! Stores values
	st	%o2, [%o0 + y]
	st	%o3, [%o0 + z]
	ret
	restore

expand:

	! Multiplies the pointed-to's structs radius by the specified value
	save	%sp, -96, %sp
	ld	[%fp-64], %l1		! Gets pointer to struct
	ld	[%l1 + r], %o0		! Gets value of r
	call	.mul			! Multiplies r by constant
	mov	%i0, %o1
	st	%o0, [%l1+r]		! Stores value of r
	ret
	restore

equal:

	! Checks if two structs are equal by checking each of their fields against each other
	save	%sp, -96, %sp

	mov	%i0, %l0		! Gets pointers to structs
	mov	%i1, %l1

	mov	FALSE, %i0		! Puts FALSE in return slot

	! Loads values of r
	ld	[%l0 + r], %l3
	ld	[%l1 + r], %l4
	cmp	%l3, %l4
	bne	notEqual
	nop

	! Loads pointers to Point
	ld	[%l0], %l0
	ld	[%l1], %l1

	! Loads values of x,y,z
	ld	[%l0 + x], %l3
	ld	[%l1 + x], %l4
	cmp	%l3, %l4
	bne	notEqual
	nop

	ld	[%l0 + y], %l3
	ld	[%l1 + y], %l4
	cmp	%l3, %l4
	bne	notEqual
	nop
	
	ld	[%l0 + z], %l3
	ld	[%l1 + z], %l4
	cmp	%l3, %l4
	bne	notEqual
	nop

	mov	TRUE, %i0		! Puts TRUE in return slot

	notEqual:

	ret
	restore

printSphere:

	! Prints out the details of the sphere in the specified format
	save	%sp, -96, %sp	

	mov	%i0, %o0
	ld 	[%i1], %o1		! Moves struct name into position	

	! Loads values of struct to be printed
	ld	[%o0 + r], %o5
	ld	[%o0], %l0
	ld	[%l0 + x], %o2
	ld	[%l0 + y], %o3
	ld	[%l0 + z], %o4	

	set	sphereprint, %o0
	call	printf
	nop

	ret
	restore

main:
	define(first, %l0)
	define(second, %l1)

	! Allocates memory and defines pointers to start of structs
	save 	%sp, -136, %sp
	sub	%fp, 40, first
	sub	%fp, 32, %l2
	st	%l2, [first]		! Sets pointer to Point struct
	sub	%fp, 20, second
	sub	%fp, 12, %l2
	st	%l2, [second]

	! Sets the two structs equal to newSphere
	call	newSphere
	nop
	ld	[%sp+64], %o0		! Gets pointer to newSphere
	ld	[%o0+r], %o4		! Loads values of newSphere
	ld	[%o0], %o0
	ld	[%o0+x], %o1
	ld	[%o0+y], %o2
	ld	[%o0+z], %o3
	st	%o4, [first+r]		! Sets values of struct equal to newSphere
	ld	[first], %o0
	st	%o1, [%o0+x]
	st	%o2, [%o0+y]
	st	%o3, [%o0+z]

	call	newSphere
	nop
	ld	[%sp+64], %o0		! Gets pointer to newSphere
	ld	[%o0+r], %o4		! Loads values of newSphere
	ld	[%o0], %o0
	ld	[%o0+x], %o1
	ld	[%o0+y], %o2
	ld	[%o0+z], %o3
	st	%o4, [second+r]		! Sets values of struct equal to newSphere
	ld	[second], %o0
	st	%o1, [%o0+x]
	st	%o2, [%o0+y]
	st	%o3, [%o0+z]

	! Prints out the sphere variable values by calling printSphere
	set	startlabel, %o0
	call	printf
	nop
	set	name + (0 << 2), %o1	! Sets name into %o1
	call	printSphere
	mov	first, %o0
	set	name + (1 << 2), %o1
	call	printSphere
	mov	second, %o0

	! Checks if the spheres are equal to each other by calling equal
	mov	first, %o0
	call 	equal
	mov	second, %o1
	! Branches to noChange if the spheres are not equal
	cmp	%o0, TRUE
	bne	noChange
	nop

	! Changes the first sphere's center coordinates by calling move
	st	first, [%sp+64]		! Sets struct pointer to first
	mov	-5, %o0			! Sets delta values as parameters
	mov	3, %o1
	mov	2, %o2
	call	move
	nop

	! Multiplies the second sphere's radius by 8 by calling expand
	st	second, [%sp+64]	! Sets struct pointer to second
	mov	8, %o0			! Sets constant as parameter
	call	expand
	nop

	noChange:

	! Prints out sphere variable values by calling printSphere	
	set	endlabel, %o0
	call	printf
	nop
	set	name + (0 << 2), %o1
	call	printSphere
	mov	first, %o0
	set	name + (1 << 2), %o1
	call	printSphere
	mov	second, %o0
	
	mov		1, %g1
	ta		0
