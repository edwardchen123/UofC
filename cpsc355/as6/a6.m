! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Assignment 6

define(OPEN, 5)
define(READ, 3)
define(FD, %l0)

.global main

output:		.asciz		"%f	%f\n"
		.align 4

col:		.asciz		"Input:		Cube Root: \n		\n"
		.align 4

! Allocates space for double values
	.section	".data"
	.align		8
	.global		input, guess, three, precis
input:	.double		0r0.0
guess:	.double		0r0.0
precis:	.double		0r1.0e-10
three:	.double		0r3.0
	.section	".text"	

main:

	save	%sp, -96, %sp				! Allocates memory for window
	! Prints out column labels
	set	col, %o0
	call	printf
	nop
	ld	[%i1+4], %o0				! Load filename into %o0
	clr	%o1					! flags = 0
	clr	%o2					! perms = 0
	mov	OPEN, %g1				! Moves OPEN instruction into %g1
	ta	0					! Opens file
	mov	%o0, FD					! Stores file descriptor

readFile:
	
	mov	READ, %g1				! Moves READ instruction into %g1
	mov	FD, %o0					! Moves file descriptor into %o0
	set	input, %o1				! Moves pointer to input storage into %o1
	mov	8, %o2					! Moves number of bytes to be read into %o2
	ta	0					! Reads from file
	cmp	%o0, 0					! Checks number of bytes read
	ble	EOF					! Branches to EOF if bytes read <= 0
	nop
	call	cubeRoot				! Finds the cube root if the input is valid
	nop
	! Prints out the values of the input and the cube root
	set	output, %o0
	set	input, %o2
	ld	[%o2], %o1
	ld	[%o2+4], %o2
	set	guess, %o4
	ld	[%o4], %o3
	ld	[%o4+4], %o4
	call	printf
	nop
	ba	readFile				! Branches to start of loop
	nop
	
cubeRoot:

	save	%sp, -96, %sp				! Allocates memory for window
	! Defines variables
	define(in, %f0)
	define(pr, %f2)
	define(thr, %f4)
	define(err, %f6)
	define(x, %f8)
	define(y, %f10)
	define(dy, %f12)
	define(absdy, %f14)
	define(dydx, %f16)
	! Sets pointer values and loads values
	set	input, %l0
	ldd	[%l0], in
	set	precis, %l1
	ldd	[%l1], pr
	set	three, %l2
	ldd	[%l2], thr
	set	guess, %l3

	! Calculates error tolerance
	fmuld	in, pr, err

	! Initializes x = input / 3
	fdivd	in, thr, x				! Calculates x = input/3.0

	Newton:
		
		! y = x^3
		fmuld	x, x, y		
		fmuld	x, y, y		
		! dy = y - input
		fsubd	y, in, dy

		! Calculates |dy|
		fabss	dy, absdy
		fmovs	%f13, %f15

		! Compares |dy| to error tolerance
		fcmpd	absdy, err
		nop
		fble	precise
		nop

		! dydx = 3.0*x^2
		fmuld	x, x, dydx
		fmuld	thr, dydx, dydx

		! x = x - dy/dydx
		fdivd	dy, dydx, dy
		fsubd	x, dy, x

		ba	Newton
		nop
		
	precise:
	
		! Stores value of x and returns
		std	x, [%l3]
		ret
		restore

EOF:

	mov	1, %g1
	ta	0
