! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Assignment 5a

define(MAXVAL, 400)	! MAXVAL = 4*100

! Define static variables
	.section	".data"
	.global		sp, val
sp:	.word		0
val:	.skip		MAXVAL
	.section	".text"	

define(top, %l1)
define(bottom, %l2)

fullerror:	.asciz		"error: stack full\n"
		.align 4

emptyerror:	.asciz		"error: stack empty\n"
		.align 4

.global push
push:
	save	%sp, -96, %sp				! Allocates memory for window
	set	sp, %l0					! Sets sp label in %l0
	ld	[%l0], top				! Loads offset of top of stack into top
	cmp	top, MAXVAL				! Compares offset to max offset
	bge	stackfull				! If stack is full, branches to stackfull
	nop
		set	val, bottom			! Sets val label in bottom
		st	%i0, [bottom+top]		! Stores input value in bottom+top
		add	top, 4, top			! Increments top by 4
		st	top, [%l0]			! Stores new top value in sp
		ret
		restore
	
	stackfull:
		! Prints error
		set	fullerror, %o0
		call	printf
		nop
		call	clear				! Clears the stack
		mov	0, %i0				! Moves 0 into return register
		ret
		restore

.global pop
pop:

	save	%sp, -96, %sp				! Allocates memory for window
	set	sp, %l0					! Sets sp label in %l0
	ld	[%l0], top				! Loads offset of top of stack into top
	cmp	top, 0					! Compares offset to min offset
	ble	stackempty				! If stack is empty, brances to stackempty
	nop
		set	val, bottom			! Sets val label in bottom
		sub	top, 4, top			! Decrements top by 4
		ld	[bottom+top], %i0		! Loads top value of stack
		st	top, [%l0]			! Stores new top value in sp
		ret
		restore
	
	stackempty:
		! Prints error
		set	emptyerror, %o0
		call	printf
		nop
		call	clear				! Clears the stack
		mov	0, %i0				! Moves 0 into return register
		ret
		restore

.global clear
clear:

	save	%sp, -96, %sp				! Allocates memory for window
	set	sp, %l0					! Sets sp label in %l0
	mov	0, %l1					! Sets %l1 = 0
	st	%l1, [%l0]				! Stores 0 in sp
	ret
	restore
	
.global getop
getop:
	
	save	%sp, -96, %sp				! Allocates memory for window
	! Define local variables
	define(i, %l0)
	define(c, %l1)
	define(s, %i0)
	define(lim, %i1)

	! Reads until something other than ' ', '\t', and '\n' is reached
	trim:
		! c = getch
		call	getch
		nop
		mov	%o0, c
		! Check if c = ' '
		cmp	c, ' '
		be	trim
		nop
		! Check if c = '\t'
		cmp	c, '\t'
		be	trim
		nop
		! Check if c = '\n'
		cmp	c, '\n'
		be	trim
		nop

	! If c < '0' or c > '9', c is returned
	cmp	c, '0'	
	bl	stop
	nop
	cmp	c, '9'	
	bg	stop
	nop
	ba	continue
	nop
	stop:		
		mov	c, %i0
		ret
		restore
	continue:
	
	! for (int i = 0; c >= '0' && c <= '9'; i++), s[i] = c
	stb	c, [s+0]				! set s[0] = c
	mov	1, i					! initialize i to 1
	ba 	setchartest
	setchar:
		cmp	i, lim				! Check if i < lim
		bge	outoflim
		nop
			stb	c, [s+i]		! Set s[i] = c
		outoflim:	
			inc	i			! increment i
	setchartest:
		call	getchar				! c = getchar()
		nop
		mov	%o0, c	
		cmp	c, '0'	
		bl	exitsetchar
		nop
		cmp	c, '9'
		ble	setchar
		nop		
	exitsetchar:

	! if (i < lim), return NUMBER ('0' = 48)
	cmp	i, lim
	bge	else
	nop
		mov	c, %o0			
		call	ungetch				! Push c onto buffer
		nop
		mov	0, %l2			
		stb	%l2, [s+i]			! Put null terminator in s
		mov	'0', %i0			! return NUMBER
		ret
		restore
	! else, read to end of line and return TOOBIG
	else:
		ba endlinetest
		nop
		
		endline:
			call	getchar			! c = getchar()
			nop
			mov	%o0, c
		endlinetest:
			cmp 	c, 10			! check if c = newline
			be	exitendline
			nop
			cmp 	c, -1			! check if c = EOF
			bne	endline
			nop
		exitendline:
			mov	0, %l2
			dec	lim
			stb	%l2, [s+lim]		! Put null terminator in s
			mov	'9', %i0		! Return TOOBIG
			ret
			restore

define(BUFSIZE, 100) 
.section	".data"
	.global		bufp, buf
bufp:	.word		0
buf:	.skip		BUFSIZE
	.section	".text"

manyerror:	.asciz		"ungetch: too many characters\n"
		.align 4

.global getch
getch:

	save	%sp, -96, %sp			! Allocates memory for window
	set	bufp, %l0			! Sets bufp into %l0
	ld	[%l0], top			! Loads bufp into top
	cmp	top, 0
	ble	retchar
	nop
		set	buf, bottom		! Sets buf in bottom
		dec	top
		ld	[bottom+top], %i0	! Loads bottom + top
		st	top, [%l0]
		ret
		restore
	retchar:
		call	getchar
		nop
		mov	%o0, %i0
		ret
		restore

.global ungetch
ungetch:

	save	%sp, -96, %sp			! Allocates memory for window
	set	bufp, %l0			! Sets bufp into %l0
	ld	[%l0], top			! Loads bufp into top
	cmp 	top, BUFSIZE			! Compares top to BUFSIZE
	ble	pushc
	nop
		! Print error
		set	manyerror, %o0
		call	printf
		nop
		ret
		restore
	pushc:
		set	buf, bottom		! Sets buf in bottom
		st	%i0, [bottom+top]	! stores c in bottom+top
		inc	top			! Increments top
		st	top, [%l0]		! Updates value of bufp
		ret
		restore
