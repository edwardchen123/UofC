;-------------------------------------------------------------------------------
; CPSC 325 - Winter 2011
; Assignment 2 Part 1 - Assembler Hash Table Collisions
; Andrew Helwer and Julia Zochodne
; February, 2011
;-------------------------------------------------------------------------------

[GLOBAL mystart]

extern _fopen                   ;Imports C-function for opening file.
extern _fclose                  ;Imports C-function for closing file.
extern _fgetc                   ;Imports C-function for reading a character.
extern _fgets                   ;Imports C-function for reading a string.
extern _printf                  ;Imports C-function for output to screen.

%macro beginProc 0		; boilerplate steps at beginning of procedures
    push	ebp             ; pushes EBP to save value for frame
    mov     	ebp, esp        ; sets EBP to stack pointer value
    push	ebx		; pushes all general registers except EAX
    push	ecx		
    push	edx
    push	edi
    push	esi
%endmacro

%macro endProc 0		; boilerplate steps at end of procedures
    pop		esi		; pops all general registers except EAX
    pop		edi
    pop		edx
    pop		ecx
    pop		ebx
    pop     	ebp             ; sets value of ebp to old value
    ret                		; pops and returns to instruction
%endmacro

;----------------------------------------------------------------------------
[SECTION .text]
;----------------------------------------------------------------------------

; code belongs in this section starting here
;----------------------------------------------------------------------------
;The mystart section in this program opens the file specified in the .data
;section. It also returns a pointer to this file in a register.
;----------------------------------------------------------------------------

mystart:
        push dword modeR                ;Pushes read mode arg onto stack
        push dword filename             ;Pushes filename arg onto stack
        call _fopen                     ;Opens file using C function
        add esp, 8                      ;Realigns stack to prevent memory leaks
        mov [filePtr], eax              ;Assign returned file pointer to variable
        mov ebx, 1
;----------------------------------------------------------------------------
;The readLoop section reads strings up to a maximum number of characters
;into the buffer specified in the .data section. If an error is returned by
;the C-function fgets, then the end of file has been reached and the program
;jumps to the close section.
;----------------------------------------------------------------------------

readLoop:
        push dword [filePtr]          ;Specifies pointer to file
        push dword 110                ;Specifies max number of chars to read.
        push dword inBuff             ;Specifies buffer to read into
        call _fgets                   ;Reads string into buffer
        add esp, 12                   ;Realigns stack to prevent memory leaks
        inc ebx
        cmp eax, 0                 ;Checks whether end of file has been reached
        je close                   ;Jump to close file section if EOF has been reached

;----------------------------------------------------------------------------
;Section checkLine determines whether the string read into the buffer in the
;readLoop section contains a label. First, it checks whether the line is a
;comment, that is, whether the line starts with '*'. Only lines that are not
;comments contain labels. Next, as labels start at the fixed character position
;of 26, we check whether this position contains a space. Only lines
;with non-blank characters in the 26th position contain a label. When a line
;containing a label has been reached, the label counter register is updated.
;----------------------------------------------------------------------------

checkLine:
        cmp byte [inBuff], '*'        ;Checks the first condition; whether the line is a comment
        je readLoop                   ;If line is a comment, it does not contain a label. Jump so that the next line can be read.
        cmp byte [inBuff+25], ' '     ;Checks the second condition; whether the 26th char is a space.
        je readLoop                   ;If line has a space in the 26th position, it does not contain a label. Jump so that the next line can be read.

;----------------------------------------------------------------------------
;The copyLabel section copies the label from the input buffer to the array
;buffer. The string containing the label is padded so that
;it is 16 bytes in length. The line number, which is 4 bytes in length,
;is added. The array offset is updated so that subsequent entries are
;written further along in the array buffer.
;----------------------------------------------------------------------------
        
copyLabel:
        cld                            ;Clears direction flag
        mov esi, inBuff                ;Places source buffer memory address in esi
        add esi, 25
        mov edi, arrBuff               ;Places destination buffer memory address in edi
        mov eax, [const]
        add edi, eax               ;Adds offset to buffer address
        mov ecx, 12                    ;Sets counter so that 12 bytes are read
        copyElems:
        lodsb
        stosb
        loop copyElems
        mov [edi+12], dword 0    ;Pads the following 4 bytes of the string
        mov [edi+16], ebx       ;Adds the line number to the string buffer
        mov ecx, [const]        ;Loads offset into register
        add ecx, 20             ;Increments offset value
        mov [const], ecx        ;Loads updated offset into memory
        jmp readLoop



;----------------------------------------------------------------------------
;The close section displays the number of labels counted and closes the file.
;The program then jumps to the final ret instruction.
;----------------------------------------------------------------------------

close:

        push dword [filePtr]         ;Passes argument containing a pointer to the open file
        call _fclose                 ;Calls C-function to close the file
        add esp, 4                   ;Realigns stack to prevent memory leaks
        jmp end                      ;Jumps past input error section to reach end of program
        
;----------------------------------------------------------------------------
;The openError section prints out an error message if the specified
;file could not be opened successfully.
;----------------------------------------------------------------------------

end:
	call	testHashTables		; run tests on hash tables
	call	showStats		; print stats gained from tests
        ret

;-------------------------------------------------------------------------------
; Name:	
;	testHashTables
; Inputs: 
;	- None; uses variables in .bss
; Returns:
;	- Void
; Description:
;	Hashes elements in input to hash tables of varying lengths, then prints
;	out statistics on the best and worse hash tables in terms of collisions.
;-------------------------------------------------------------------------------
testHashTables: 
beginProc
    mov		ecx, 448		; initializes counter to array length
    tableSizeLoop:
	mov	[tableSize], ecx	; updates size of hash table
	call	putAllElems             ; puts all array elements in hash table
        push	eax			; pushes number of collisions as input
	call	record			; records statistics about collisions
	add	esp, 4			; prevents memory leaks
	inc	ecx			; increment hash table size
	call	clearTable		; returns all values of table to 0
	cmp	ecx, 896		; checks termination condition
	jle	tableSizeLoop
endProc

;-------------------------------------------------------------------------------
; Name:	
;	record
; Inputs: 
;	- Number of collisions with current table size (DWORD, Integer)
; Returns:
;	- Void
; Description:
;	Updates lists of best and worst hash tables, as well as recording what
;	range of collisions this particular table size fell within.
;-------------------------------------------------------------------------------
record:
beginProc
    mov		eax, DWORD [tableSize]	; moves table size into EAX
    mov		ebx, DWORD [ebp+8]	; moves number of collisions into EBX
    mov		ecx, DWORD best		; moves pointer to best into ECX
    mov		edx, 0			; initializes offset to 0
    ; the following determines if the record belongs in the top ten
    sortBestLoop:
	mov	edi, DWORD [ecx+edx+4]	; reads in collisions
	cmp	ebx, edi		; checks ECX against recorded collisions
	jl	better
	cmp	edi, 0			; checks if entry is empty
	je	better
	add	edx, 8			; increments offset
	cmp	edx, 80			; test out of bounds
	jl	sortBestLoop
	jmp	notBetter		; record does not make top 10
    ; inserts record at that point and shifts other records down by one
    better:
	mov	edi, DWORD [ecx+edx]	; records old table size
	mov	esi, DWORD [ecx+edx+4]	; records old number of collisions
	mov	[ecx+edx], DWORD eax	; stores new table size
	mov	[ecx+edx+4], DWORD ebx	; stores new number of collision
	mov	eax, edi		; moves old table size to EAX
	mov	ebx, esi		; moves old number of collisions to EBX
        add	edx, 8			; increments EDX
	cmp	edx, 80			; test out of bounds
	jl	better
    notBetter:
    
    mov		eax, DWORD [tableSize]	; moves table size into EAX
    mov		ebx, DWORD [ebp+8]	; moves number of collisions into EBX
    mov		ecx, DWORD worst	; moves pointer to worst into ECX
    mov		edx, 0			; initializes offset to 0
    ; the following determines if the record belongs in the bottom ten
    sortWorstLoop:
	mov	edi, DWORD [ecx+edx+4]	; reads in collisions
	cmp	ebx, edi		; checks ECX against recorded collisions
	jg	worse
	add	edx, 8			; increments offset
	cmp	edx, 80			; test out of bounds
	jl	sortWorstLoop
	jmp	notWorse		; record does not make bottom 10
    ; inserts record at that point and shifts other records down by one
    worse:
	mov	edi, DWORD [ecx+edx]	; records old table size
	mov	esi, DWORD [ecx+edx+4]	; records old number of collisions
	mov	[ecx+edx], DWORD eax	; stores new table size
	mov	[ecx+edx+4], DWORD ebx	; stores new number of collision
	mov	eax, edi		; moves old table size to EAX
	mov	ebx, esi		; moves old number of collisions to EBX
	add	edx, 8			; increments EDX
        cmp	edx, 80			; test out of bounds
	jl	worse
    notWorse:
    
    mov		eax, DWORD [tableSize]	; moves table size into EAX
    mov		ebx, DWORD [ebp+8]	; moves number of collisions into EBX
    mov		ecx, DWORD groups	; moves pointer to groups into ECX
    mov		edx, 0			; initializes offset to 0
    mov		esi, 100		; initialize upper bound on range to 50
    
    ; the following places the record in the proper group range
    putInGroup:
	cmp	ebx, esi		; checks if number falls within group
	jle	incGroup
        add	edx, 4			; increases position counter
	add	esi, 100		; increases upper bound on range
        cmp     esi, 1000               ; puts upper limit
        je      incGroup
	jmp	putInGroup
	
    incGroup:
	mov	edi, [ecx+edx]		; moves group size into EDI
	inc	edi			; increase group size by one
	mov	[ecx+edx], edi		; stores group size
    
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:	
;	clearTable
; Inputs: 
;	- None; uses variables in .bss
; Returns:
;	- Void
; Description:
;	Resets the hash table by setting every element to 0.
;-------------------------------------------------------------------------------
clearTable:
beginProc
    mov		edi, hashTable	; puts pointer to hash table in EDI
    mov		eax, 0		; sets value to be read
    mov		ecx, 896	; initializes loop counter
    zeroElems:
	stosd			; 4 bytes
	stosd			; 8 bytes
	stosd			; 12 bytes
	stosd			; 16 bytes
	stosd			; 20 bytes
	stosd			; 24 bytes
    loop	zeroElems
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	putAllElems
; Inputs:
;	- Size of hashTable (DWORD, Integer)
; Returns:
;	- Total number of collisions generated (DWORD, Integer) in EAX
; Description:
;	Finds the number of collisions generated during the hashing of all
;	elements of the array into a hash table of a certain size.
;-------------------------------------------------------------------------------
putAllElems:
beginProc
    mov		ebx, DWORD arrBuff 	; sets EBX to pointer to label array
    mov		ecx, 0			; initializes offset to 0
    mov		edx, 0			; initializes collisions to 0
    
    iterateLoop:
	mov	eax, ebx		; copies pointer to label array into EAX
	add	eax, ecx		; offsets to current element
	push	eax			; pushes pointer element as input
	call	putElem			; puts current element in hashTable
	add	esp, 4			; prevents memory leaks
	add	edx, eax		; updates total number of collisions
	add	ecx, 20			; increments offset
	cmp	ecx, 8960		; checks termination condition
	jl	iterateLoop
	
    mov		eax, edx		; returns collisions in EAX
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	putElem
; Inputs:
;	- Pointer to element to be hashed (DWORD, Pointer)
; Returns:
;	- Number of collisions generated (DWORD, Integer) in EAX
; Description:
;	Finds the number of collisions generated during the hashing of a single 
;	element into a hash table of a certain size.
;-------------------------------------------------------------------------------
putElem:
beginProc
    mov 	ebx, DWORD [ebp+8]	; gets pointer to element
    
    ; the following finds the index the specified element has been hashed to
    push	ebx			; pushes element as input
    call	hashElem		; hashes element to index pointer
    add		esp, 4			; prevents memory leaks
    mov		ecx, eax		; moves hash value into ECX
    
    ; the following checks if the index is blank and so if there are collisions
    push	ecx			; pushes index hashed to as input
    call	indexTaken		; checks if location is taken
    add		esp, 4			; prevents memory leaks
    cmp		eax, 0			; if EAX==0, location is blank
    je		noCol			; jump to no collisions if blank
    
    ; the following handles the case of a collision occurring
    push	ecx			; pushes index hashed to as input
    push	ebx			; pushes pointer to element as input
    call	findNextOpen		; puts element in next open index
    add		esp, 8			; prevents memory leaks
    endProc				; returns with num collisions in EAX
    
    ; the following handles the case of no collision occurring
    noCol:
    push	ecx			; pushes index hashed to as input
    push	ebx			; pushes pointer to element as input
    call	copyElem		; copies element into offset
    add		esp, 8			; prevents memory leaks
    mov		eax, 0			; sets num collisions to 0
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	hashElem
; Inputs:
;	- Pointer to element to be hashed (DWORD, Pointer)
; Returns:
;	-Pointer to index hash table (DWORD, Integer) in EAX
; Description:
;	Hashes the element to an integer value.
;-------------------------------------------------------------------------------
hashElem:
beginProc
    mov		esi, DWORD [ebp+8]	; loads pointer to element to be hashed
    mov		ecx, DWORD [tableSize]	; loads table size
    mov		edx, 0			; initializes hash value to 0
    
    ; the following adds up the binary values of the label name
    lodsd
    add		edx, eax
    lodsd
    add		edx, eax
    lodsd
    add		edx, eax
    lodsd
    add		edx, eax
    
    ; the following converts the hash value into an index pointer
    mov		eax, edx		; copies hash value into EAX
    mov		edx, 0			; zeroes out EDX 
    div		ecx			; reduces hash modulo table length
    mov		eax, edx		; moves table offset into EAX
    mov		edx, 24			; copies 24 into EDX
    mul		edx			; converts index into bytes offset
    mov		edx, DWORD hashTable	; loads pointer to table into EDX
    add		eax, edx		; EAX now contains pointer to index
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	findNextOpen
; Inputs:
;	- Pointer to element to be put in table (DWORD, Pointer)
;	- Pointer to hash index into the hash table (DWORD, Pointer)	
; Returns:
;	- Length of chain the value is stored in (DWORD, Integer) in EAX
; Description:
;	Searches the hash table for the next open spot and updates the chain
;	accordingly. Copies element into open spot. Uses algorithm described
;	in assignment specification.
;-------------------------------------------------------------------------------
findNextOpen:
beginProc
    mov		ebx, DWORD [ebp+8]	; loads pointer to element
    mov		edi, DWORD [ebp+12]	; loads hash value of element
    mov		ecx, 0			; set numCollisions = 0

    ; the following traces the chain to its end
    findChainEnd:
	mov	esi, edi		; set current = next
	add	edi, 20			; offset to next-in-chain pointer
	mov	edi, DWORD [edi]	; set next = current.next
	inc	ecx			; increments number of collisions
    findChainEndTest:
	cmp	edi, 0			; checks if next==null
	jne	findChainEnd
	
    mov		edi, esi		; sets next = current
    add		edi, 20			; offsets to 'next' pointer of current
    
    ; the following scans linearly through the table to find an opening
    scanTable:
	push	esi			; pushes current pointer as input
	call	getNextIndex		; gets next index in hash table
	add	esp, 4			; prevents memory leaks
	mov	esi, eax		; sets ESI to next index in hash table
    scanTableTest:
	push	esi			; pushes current pointer as input
	call	indexTaken		; checks if current index is empty
	add	esp, 4			; prevents memory leaks
	cmp	eax, 1			; if EAX==1, index is taken
	je	scanTable
	
    mov		[edi], esi		; sets next pointer to new chain end
    mov		eax, ecx		; moves number of collisions into EAX
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	getNextIndex
; Inputs:
;	- Pointer to current index (DWORD, Pointer)
; Returns:
;	- Pointer to next index in table (DWORD, Pointer)
; Description:
;	Returns the pointer to the next index in the hash table, wrapping around
;	if at the end.
;-------------------------------------------------------------------------------
getNextIndex:
beginProc
    mov		ebx, DWORD [ebp+8]	; loads index pointer
    mov		ecx, DWORD [tableSize]	; loads table size
    
    mov		eax, ecx		; copies table size into EAX
    mov		ecx, 24			; copies 24 into ECX
    mul		ecx			; converts table size into byte length
    mov		edx, DWORD hashTable	; loads pointer to hash table
    add		eax, edx		; points to very end of hash table
    add		ebx, 24			; increments to next index of hash table
    cmp		ebx, eax		; checks for rollover
    jl		noRollover
    mov		ebx, edx		; puts pointer to start of table in EAX
noRollover:
    mov		eax, ebx		; returns value in EAX
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	indexTaken
; Inputs:
;	- Pointer to index to be checked (DWORD, Pointer)
; Returns:
;	- 0 if blank, 1 if filled (DWORD, Integer) in EAX
; Description:
;	Checks if the specified location is taken or open in the hash table.
;-------------------------------------------------------------------------------
indexTaken:
beginProc
    mov		esi, DWORD [ebp+8]	; loads index pointer
    mov		ebx, 1			; sets filled = true
    ; the following checks all 24 bytes to see if they are zeroed
    lodsd
    cmp		eax, 0
    jne		notEmpty
    lodsd
    cmp		eax, 0
    jne		notEmpty
    lodsd
    cmp		eax, 0
    jne		notEmpty
    lodsd
    cmp		eax, 0
    jne		notEmpty
    lodsd
    cmp		eax, 0
    jne		notEmpty
    lodsd
    cmp		eax, 0
    jne		notEmpty
    
    mov		ebx, 0			; sets filled = false
notEmpty:
    mov		eax, ebx		; moves filled boolean value into EAX
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	copyElem
; Inputs:
;	- Pointer to element to be copied (DWORD, Pointer)
;	- Pointer to destination (DWORD, Pointer)
; Returns:
;	- Void
; Description:
;	Copies the element into the hash table at the specified location.
;-------------------------------------------------------------------------------
copyElem:
beginProc
    mov		esi, DWORD [ebp+8]	; load source pointer
    mov		edi, DWORD [ebp+12]	; load destination pointer
    
    ; the following copies 20 bytes of data from source to destination
    ; 4 bytes
    lodsd
    stosd
    ; 8 bytes
    lodsd
    stosd
    ; 12 bytes
    lodsd
    stosd
    ; 16 bytes
    lodsd
    stosd
    ; 20 bytes
    lodsd
    stosd
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:	
;	showStats
; Inputs: 
;	- None; uses variables in .bss
; Returns:
;	- Void, although output is printed to command line
; Description:
;	Prints out various statistics regarding number of hash table collisions.
;-------------------------------------------------------------------------------
showStats;
beginProc

    ; the following prints the lengths of the ten worst hash tables
    mov		eax, worstL		; moves text into eax
    push	eax			; pushes text as input
    call	printString		; prints text
    add		esp, 4			; prevents memory leaks

    mov         ecx, 0			; initializes loop counter
    mov         esi, worst		; gets pointer to worst in ESI
    pWorstL:
    lodsd				; loads table length into EAX
    push        eax			; pushes table length as input
    call	printNum		; prints table length
    add		esp, 4			; prevents memory leaks
    add		esi, 4			; skips ESI past num collisions
    inc ecx                     	; increments counter
    cmp ecx, 10                  	; checks for termination
    jl  pWorstL

    ; the following prints the lengths of the ten best hash tables
    mov		eax, bestL		; moves text into eax
    push	eax			; pushes text as input
    call	printString		; prints text
    add		esp, 4			; prevents memory leaks

    mov         ecx, 0			; initializes loop counter
    mov         esi, best		; gets pointer to best in ESI
    pBestL:
    lodsd				; loads table length into EAX
    push        eax			; pushes table length as input
    call	printNum		; prints table length
    add		esp, 4			; prevents memory leaks
    add		esi, 4			; skips ESI past num collisions
    inc ecx                     	; increments counter
    cmp ecx, 10                  	; checks for termination
    jl  pBestL
    
    ; the following prints the collisions of the five worst hash tables
    mov		eax, worstC		; moves text into eax
    push	eax			; pushes text as input
    call	printString		; prints text
    add		esp, 4			; prevents memory leaks

    mov         ecx, 0			; initializes loop counter
    mov         esi, worst		; gets pointer to worst in ESI
    add		esi, 4			; points ESI to num collisions
    pWorstC:
    lodsd				; loads num collisions into EAX
    push        eax			; pushes collisions as input
    call	printNum		; prints collisions
    add		esp, 4			; prevents memory leaks
    add		esi, 4			; skips ESI past table length
    inc ecx                     	; increments counter
    cmp ecx, 5                  	; checks for termination
    jl  pWorstC
    
    ; the following prints the collisions of the five best hash tables
    mov		eax, bestC		; moves text into eax
    push	eax			; pushes text as input
    call	printString		; prints text
    add		esp, 4			; prevents memory leaks

    mov         ecx, 0			; initializes loop counter
    mov         esi, best		; gets pointer to best in ESI
    add		esi, 4			; points ESI to num collisions
    pBestC:
    lodsd				; loads num collisions into EAX
    push        eax			; pushes collisions as input
    call	printNum		; prints collisions
    add		esp, 4			; prevents memory leaks
    add		esi, 4			; skips ESI past table length
    inc ecx                     	; increments counter
    cmp ecx, 5                  	; checks for termination
    jl  pBestC

    ; generates a list of output format strings
    mov		eax, groupLabels
    mov		[eax], DWORD stat0
    mov		[eax+4], DWORD stat1
    mov		[eax+8], DWORD stat2
    mov		[eax+12], DWORD stat3
    mov		[eax+16], DWORD stat4
    mov		[eax+20], DWORD stat5
    mov		[eax+24], DWORD stat6
    mov		[eax+28], DWORD stat7
    mov		[eax+32], DWORD stat8
    mov		[eax+36], DWORD stat9
    ; prints out number of tables in each range of collisions
    mov		ebx, groups		; move pointer to groups into EBX
    mov		ecx, 0			; intialize offset to 0
    mov		edx, groupLabels	; move pointer to groupLabels into EDX
    groupOutput:
	mov	eax, [groupLabels+ecx]	; read in output format string
	push	eax			; pushes format string as input
	call	printString		; prints format string
	add	esp, 4			; prevents memory leaks
	mov	eax, [groups+ecx]	; load number of tables in range
	push	eax			; pushes number as input
	call	printNum		; prints number
	add	esp, 4			; prevents memory leak
	add	ecx, 4			; increments offset
	cmp	ecx, 40			; checks for out of bounds
	jl	groupOutput
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	printString
; Inputs:
;	- Pointer to string to be printed (DWORD, Pointer)
; Returns:
;	- Void
; Description:
;	Prints out the specified string. This function, and the printNum
;	function, were written to cope with frustrating segfaults in the
;	malloc() function of _printf by simply replacing the implementation
;	with a custom-written one, albeit without the ability to specify
;	a string that contains an integer passed as a variable.
;-------------------------------------------------------------------------------
printString:
beginProc
	mov     ah, 09h                 ; selects write string function
        mov     edx, [ebp+8]            ; puts specified prompt in EDX
        int     0f1h                    ; calls OS interrupt 0F1H
endProc
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
; Name:
;	printNum
; Inputs:
;	- Number to be printed (DWORD, Integer)
; Returns:
;	- Void
; Description:
;	Prints out the specified number. Note that this is a modified version of
;	binAscii used in assignment 1, which is itself a fixed version of the
;	one found in buggy.asm written by Dr. Walpole.
;-------------------------------------------------------------------------------
printNum:
beginProc
    mov		eax, [ebp+8]	; loads number to be printed
    mov		ecx, 10		; sets base to 10
    mov		ebx, outbuff	; specifies output buffer
	
    mov  	edi, ebx        ; put pointer to output string in EDI
    test 	eax, 0ffffffffh
    jns  positive
    mov  byte [edi], '-'     	; store minus sign in output string
    inc  edi

    neg  eax                 	; will this work OK with 8000000H?
positive:
    push dword 0             	; push marker, keeping stack dword aligned
looop:
    mov  edx, 0              	; NOTE: HAD TO FIX THIS ONE, CUZ THE STUPID
				; INTEL CHIP INSISTS ON GENERATING A DIVIDE
				; ERROR EXCEPTION IF QUOTIENT TOO BIG!!!
			    
    div  ecx                 	; divide base into R:Q where EDX will
				; contain the remainder and EAX the
				; dividend then quotient
    cmp  edx, 9
    ja  letter
    add  edx, '0'            	; most convenient to do ASCII conversion here
				; add ASCII '0'
    jmp  puush               	; so we can terminate our popping off stack
letter:
    add  edx, 'A'            	; easily: we can have digits of zero which
    sub  edx, 10             	; couldn't be distinguished from the marker
puush:
    push edx                 	; push the character
    test eax, 0ffffffffh     	; if quotient is zero we are done
    jnz  looop

outloop:
    pop  eax                 ; pop digits
    mov  [edi], al           ; and store
    inc  edi                 ; the null is also popped and stored
    test al, 0ffh            ; test for null
    jnz  outloop

; prints out the number, which is now in ASCII

    mov  esi, outbuff        ; load ESI with address of buffer
    cld                      ; clear direction flage for forward scan
    mov  ah, 02h             ; select write character function
loop1:   lodsb               ; get next byte   (NB this loop could be
    test al, 0ffh            ; test for null    more efficiently coded!)
    jz   done
    mov  dl, al              ; copy character into DL for function
    int  0f1h                ; call OS interrupt 0F1H
    jmp  loop1               ; ending in JMP is inefficient
done:

; prints a newline
    mov	eax, nl
    push	eax
    call	printString
    add	esp, 4
endProc         
;-------------------------------------------------------------------------------

;-------------------------------------------------------------------------------
[SECTION .data] ; initialized data goes here
;-------------------------------------------------------------------------------
modeR db "r", 0                        ;Initializes a read-mode variable
filename db "ASM68_CW.txt",0           ;Specifies the name of the input file
output   db "testout.txt",0
openErrStr db "error opening file", 13, 10  ;Specifies IO error message.
showLabel db "%s", 13, 10          ;Specifies string for displaying count
showNum db "%d", 13, 10         ;Specifies string for displaying line number
; specifies labels for various ranges
stat0   db      "Number of tables with 0-99 collisions: $"
stat1   db      "Number of tables with 100-199 collisions: $"
stat2   db      "Number of tables with 200-299 collisions: $"
stat3   db      "Number of tables with 300-399 collisions: $"
stat4   db      "Number of tables with 400-499 collisions: $"
stat5   db      "Number of tables with 500-599 collisions: $"
stat6   db      "Number of tables with 600-699 collisions: $"
stat7   db      "Number of tables with 700-799 collisions: $"
stat8   db      "Number of tables with 800-899 collisions: $"
stat9   db      "Number of tables with 900+ collisions: $"
bestL   db      "Best ten table lengths: ", 13, 10, "$"
worstL	db	"Worst ten table lengths: ", 13, 10, "$"
bestC	db	"Best five number of collisions: ", 13, 10, "$"
worstC	db	"Worst five number of collisions: ", 13, 10, "$"
nl	db	13, 10, "$"							     
							    
;-------------------------------------------------------------------------------
[SECTION .bss] ; zero-initialized data goes here
;-------------------------------------------------------------------------------
best		resb	80		; ten best hash tables
worst		resb	80		; ten worst hash tables
groups		resb	80		; num of tables between x-y collisions
tableSize	resb	4		; length of hash table
filePtr 	resd 	1               ; Zero initializes a file pointer variable
inBuff 		resb 	110             ; Zero initializes an input buffer variable
arrBuff 	resb 	448*20		; buffer into which labels are read
hashTable 	resb 	448*20*2	; hash table
groupLabels	resb	40		; array of strings to be printed
const           resb    4               ; constant
outbuff		resb	100		; output buffer
