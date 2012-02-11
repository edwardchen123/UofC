! CPSC 355-T01 F10
! M. Helmi Khomeirani
! Written by Andrew Helwer - 10023875
! Assignment 5b

.global main

! Define array of strings
.align 4
.global month
month:	.word	jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec

jan:	.asciz		"January"
	.align 4
feb:	.asciz		"February"
	.align 4
mar:	.asciz		"March"
	.align 4
apr:	.asciz		"April"
	.align 4
may:	.asciz		"May"
	.align 4
jun:	.asciz		"June"
	.align 4
jul:	.asciz		"July"
	.align 4
aug:	.asciz		"August"
	.align 4
sep:	.asciz		"September"
	.align 4
oct:	.asciz		"October"
	.align 4
nov:	.asciz		"November"
	.align 4
dec:	.asciz		"December"
	.align 4

! Define output labels
date:	.asciz		"%s %sth, %s\n"
	.align 4

error:	.asciz		"usage mm dd yyyy\n"
	.align 4

main:

	! Allocate memory for window
	save	%sp, -96, %sp

	! Define local variables
	define(m, %l0)
	define(d, %l1)
	define(y, %l2)
	
	! Test that there are three arguments
	cmp	%i0, 4
	bne	printerror
	nop

	! Get month
	ld	[%i1+4], %o0 
	! Convert month to integer
	call	atoi
	nop
	mov	%o0, m
	! Convert month to index of string array
	dec	m
	sll	m, 2, m

	! Get day
	ld	[%i1+8], d

	! Get year
	ld	[%i1+12], y

	! Print date
	set	date, %o0
	set	month, %o1
	add	%o1, m, %o1	! Calculate index of proper month
	ld	[%o1], %o1	! Load pointer to month string
	mov	d, %o2
	mov	y, %o3
	call	printf
	nop

	! Exits program
	ba 	exit
	nop

	printerror:

	! Prints usage error
	set	error, %o0
	call	printf
	nop

exit:

mov		1, %g1
ta		0
