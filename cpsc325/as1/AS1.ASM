;----------------------------------------------------------------------------
; CPSC 325 - Winter 2011
; Assignment 1 - Base Conversion
; Andrew Helwer and Julia Zochodne
; January 29th, 2011
;----------------------------------------------------------------------------

[GLOBAL mystart]    ; exports the start address

;----------------------------------------------------------------------------
[SECTION .text]
;----------------------------------------------------------------------------

; All code goes in this section.

mystart:

        ; STEP ONE: ASK FOR BASE OF INPUT
        push    DWORD b1prompt          ; sets b1prompt as argument to prompt
        call    prompt                  ; prints out the first prompt
        add     esp, 4                  ; prevents memory leak
        
        call    getinput                ; reads user input into buffer

        call    checkquit               ; checks if user wishes to quit
        cmp     ebx, 1                  ; compares ebx to quit code
        je      quit

        call    getbase                 ; get and process input base
        cmp     ebx, 0                  ; ask for input again
        je      mystart

        push    eax                     ; pushes bin base to stack

        ; STEP TWO: ASK FOR NUMBER
        call    getnumber               ; get and process input number
        cmp     ebx, 0                  ; ask for input again
        je      mystart
        add     esp, 4                  ; forgets base
        push    eax                     ; pushes number to stack

        ; STEP THREE: ASK FOR BASE OF OUTPUT
        push    DWORD b2prompt          ; sets b1prompt as argument to prompt
        call    prompt                  ; prints out the first prompt
        add     esp, 4                  ; prevents memory leak
        
        call    getinput                ; reads user input into buffer

        call    getbase                 ; get and process output base
        cmp     ebx, 0                  ; ask for input again
        je      mystart

        ; STEP FOUR: CONVERT NUMBER

        mov     ecx, eax                ; sets ecx = base
        pop     eax                     ; pops number to eax
        mov     ebx, outbuff            ; sets outbuffer to ebx
        call    binascii                ; converts number to base

        push    DWORD nl
        call    prompt
        add     esp, 4

        ; STEP FIVE: PRINT RESULT
        call    printoutput

        jmp     mystart                 ; loops back to start
        
        quit:
                ret
                
;----------------------------------------------------------------------------
getbase:           ; gets base of input

        call    checkneg                ; checks if input starts with '-'
        cmp     ebx, 1                  ; 1 if negative
        je      e3base1                 ; prints error if base is negative

        push    DWORD 0000000Ah         ; pushes base as input
        call    asciibin                ; parses input, returns value in eax
        add     esp, 4                  ; prevents memory leak

        cmp     eax, 2                  ; checks lower bound on base
        jl      e3base1

        cmp     eax, 36                 ; checks upper bound on base
        jg      e3base1

        cmp     ebx, 0                  ; checks for error1 code
        je      e1base1

        cmp     ebx, 1                  ; checks for error2 code
        je      e2base1

        mov     ebx, 1                  ; successful execution
        ret

        e1base1:

                push    DWORD e1        ; sets e1 as argument to prompt
                call    prompt          ; prints out error message
                add     esp, 4          ; prevents memory leak
                mov     ebx, 0          ; sets retry status
                ret
                
        e2base1:

                push    DWORD e2        ; sets e2 as argument to prompt
                call    prompt          ; prints out error message
                add     esp, 4          ; prevents memory leak
                mov     ebx, 0          ; sets retry status
                ret
                
        e3base1:

                push    DWORD e3        ; sets e3 as argument to prompt
                call    prompt          ; prints out error message
                add     esp, 4          ; prevents memory leak
                mov     ebx, 0          ; sets retry status
                ret
                
;----------------------------------------------------------------------------
getnumber:              ; gets input number

        push    ebp
        mov     ebp, esp

        push    DWORD numprompt         ; sets numprompt as argument to prompt
        call    prompt                  ; prints out the second prompt
        add     esp, 4                  ; prevents memory leak

        call    getinput                ; reads user input into buffer

        mov     ebx, DWORD [ebp+8]      ; moves input base into EBX
        push    ebx                     ; pushes base as input to binascii

        call    asciibin                ; parses input, returns value in eax
        add     esp, 4                  ; prevents memory leak

        cmp     eax, 2147483647         ; checks max value
        jg      e4num

        cmp     eax, -2147483648        ; checks min value
        jl      e4num

        cmp     ebx, 0                  ; checks for error1 code
        je      e1num

        cmp     ebx, 1                  ; checks for error2 code
        je      e2num

        cmp     ebx, 3                  ; checks for error3 code
        je      e4num

        call    checkneg                ; checks if input starts with '-'
        cmp     ebx, 1                  ; 1 if negative
        jne     nonneg

        neg     eax                     ; puts eax in 2's complement

        nonneg:

                mov     ebx,1           ; successful execution
                pop     ebp
                ret

        e1num:

                push    DWORD e1        ; sets e1 as argument to prompt
                call    prompt          ; prints out error message
                add     esp, 4          ; prevents memory leak
                mov     ebx, 0          ; sets retry status
                pop     ebp
                ret
                
        e2num:

                push    DWORD e2        ; sets e2 as argument to prompt
                call    prompt          ; prints out error message
                add     esp, 4          ; prevents memory leak
                mov     ebx, 0          ; sets retry status
                pop     ebp
                ret

        e4num:

                push    DWORD e4        ; sets e4 as argument to prompt
                call    prompt          ; prints out error message
                add     esp, 4          ; prevents memory leak
                mov     ebx, 0          ; sets retry status
                pop     ebp
                ret
                
;----------------------------------------------------------------------------
printoutput:		; This method has been taken from buggy.asm by Dr. Walpole

		 mov  esi, outbuff        ; load ESI with address of buffer
         cld                      ; clear direction flage for forward scan
         mov  ah, 02h             ; select write character function
loop1:   lodsb                    ; get next byte   (NB this loop could be
         test al, 0ffh            ; test for null    more efficiently coded!)
         jz   done
         mov  dl, al              ; copy character into DL for function
         int  0f1h                ; call OS interrupt 0F1H
         jmp  loop1               ; ending in JMP is inefficient
done:
		ret

;----------------------------------------------------------------------------
prompt:         ; prints out a prompt passed as an argument on the stack

        push    ebp                     ; pushes EBP to save value for frame
        mov     ebp, esp                ; sets EBP to stack pointer value
        
        mov     ah, 09h                 ; selects write string function
        mov     edx, [ebp+8]            ; puts specified prompt in EDX
        int     0f1h                    ; calls OS interrupt 0F1H

        pop     ebp                     ; sets value of ebp to old value
        ret                             ; pops and returns to caller instruction
        
;----------------------------------------------------------------------------
getinput:       ; reads user input into the buffer

        mov     BYTE [buffer], 98       ; specifies capacity of buffer
        mov     BYTE [buffer+1], 0      ; overwrites old values in buffer
        mov     ah, 0ah                 ; selects buffered input function
        mov     edx, buffer             ; puts address of buffer in EDX
        int     0f1h                    ; calls OS interrupt 0F1H

        ret                             ; pops and returns to caller instruction

;----------------------------------------------------------------------------
checkquit:      ; checks if quit option is selected

        movzx   eax, BYTE [buffer+2]    ; reads first char into eax

        and     eax, 000000DFh          ; capitalizes the character
        
        cmp     ax, 00000051h           ; compares input to Q
        jne     R1                      ; jumps to R1 if quit is specified

        mov     ebx, 1                  ; returns quit code
        ret

        R1:
                mov     ebx, 0          ; returns non-quit code
                ret

;----------------------------------------------------------------------------
checkneg:       ; checks if input is negative

        movzx   ebx, BYTE [buffer+2]    ; reads first char into eax

        cmp     ebx, 0000002Dh          ; compares first char to '-'
        je      rneg                    ; jumps to R1 if negative

        mov     ebx, 0                  ; returns non-negative code
        ret

        rneg:
                mov     ebx, 1          ; returns negative code
                ret
                
;----------------------------------------------------------------------------
asciibin:       ; converts ascii in buffer to binary

        push    ebp                     ; pushes EBP to save value for frame
        mov     ebp, esp                ; sets EBP to stack pointer value

        mov     eax, 0                  ; sets accumulator to 0
        mov     ebx, [ebp+8]            ; gets base
        mov     ecx, 2                  ; initializes offset counter
        label1:
        movzx   edx, BYTE [buffer+ecx]  ; looks at first char in input
        cmp     edx, 0000002Bh          ; compares first char to '+'
        je      append

        cmp     edx, 0000002Dh          ; compares first char to '-'
        jne     L1

        append:
        
                inc     ecx                     ; set offset to skip first char

        L1:
                label2:
                mov     dl, BYTE [buffer+ecx]   ; mov char to edx
                cmp     dl, '0'                 ; compare char to '0'
                jl      C1
                cmp     dl, '9'                 ; compare char to '9'
                jg      C1
                sub     dl, '0'                 ; subtracts '0' from char
                jmp     C2

                C1:
                        and     dl, 0DFh        ; capitalizes
                        cmp     dl, 'A'         ; compare char to 'A'
                        jl      term
                        cmp     dl, 'Z'         ; compare char to 'Z'
                        jg      term
                        sub     dl, 'A'         ; subtracts 'A' from char
                        add     dl, 10          ; adds 10 to digit

                C2:
                        movzx   edi, dl         ; zeroes out edi
                        cmp     edi, ebx        ; compares digit to base
                        jge     notinbase       ; error if digit >= base
                        mul     ebx             ; accum *= base
                        add     eax, edi        ; accum += digit

                inc     ecx                     ; increment loop counter
                jmp     L1                      ; jumps back to L1

        term:
                movzx   edx, dl                 ; zeroes out edx
                cmp     edx, 0000000Dh          ; checks for carriage return
                jne     invalid                 ; jumps to error if not
                mov     ebx, 2                  ; successful execution!
                pop     ebp                     ; sets value of ebp to old value
                ret
                
        invalid

                mov     ebx, 0                  ; sets error1 code
                pop     ebp                     ; sets value of ebp to old value
                ret

        notinbase:

                mov     ebx, 1                  ; sets error2 code
                pop     ebp                     ; sets value of ebp to old value
                ret

				
;----------------------------------------------------------------------------
binascii:		; This code is a fixed version of the one found in buggy.asm by Dr. Walpole
         push edx                 ; transparency
         push edi

         mov  edi, ebx            ; put pointer to output string in EDI
         test eax, 0ffffffffh
         jns  positive
         mov  byte [edi], '-'     ; store minus sign in output string
         inc  edi

         neg  eax                 ; will this work OK with 8000000H?
positive:
         push dword 0             ; push marker, keeping stack dword aligned
looop:
         mov  edx, 0              ; NOTE: HAD TO FIX THIS ONE, CUZ THE STUPID
                                  ; INTEL CHIP INSISTS ON GENERATING A DIVIDE
                                  ; ERROR EXCEPTION IF QUOTIENT TOO BIG!!!
                                  
         div  ecx                 ; divide base into R:Q where EDX will
                                  ; contain the remainder and EAX the
                                  ; dividend then quotient
         cmp  edx, 9
         ja  letter
         add  edx, '0'            ; most convenient to do ASCII conversion here
                                  ; add ASCII '0'
         jmp  puush               ; so we can terminate our popping off stack
letter:
         add  edx, 'A'            ; easily: we can have digits of zero which
         sub  edx, 10                         ; couldn't be distinguished from the marker
puush:
         push edx                 ; push the character
         test eax, 0ffffffffh     ; if quotient is zero we are done
         jnz  looop

outloop:
         pop  eax                 ; pop digits
         mov  [edi], al           ; and store
         inc  edi                 ; the null is also popped and stored
         test al, 0ffh            ; test for null
         jnz  outloop

         pop  edi                 ; transparency
         pop  edx
         ret


;----------------------------------------------------------------------------
[SECTION .data]
;----------------------------------------------------------------------------

; All initialized data goes in this section.

testy    db      "ran!",13,10

; this is the prompt for the first base
b1prompt        db    13,10,"Enter the base of the number to be entered OR ",34,"Q",34," to quit:", 13, 10, "> $"

; this is the prompt for the number to be converted
numprompt       db    13,10,"Enter a number: ", 13, 10, "> $"

; this is the prompt for the second base
b2prompt        db     13,10,"Enter the base desired for output value: ", 13, 10, "> $"

o1      db      13,10,"The BASE $"

o2      db      " number $"

o3      db      " expressed in BASE $"

o4      db      " is $"

nl      db      13,10,"$"

; error1 message
e1      db      13,10,"Invalid input! Numbers and letters only.", 13, 10,"$"

; error2 message
e2      db      13,10,"Invalid input! One or more digits are not in the correct base.", 13, 10,"$"

; error3 message
e3      db      13,10,"Invalid input! Base must be between 2 and 36.",13,10,"$"

; error4 message
e4      db      13,10,"Invalid input! Number out of bounds.",13,10,"$"
;----------------------------------------------------------------------------
[SECTION .bss]
;----------------------------------------------------------------------------

; All unitialized data goes in this section.

buffer  resb    100     ; input buffer
outbuff	resb	100	; output buffer

