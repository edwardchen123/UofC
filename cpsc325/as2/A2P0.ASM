;----------------------------------------------------------------------------
; CPSC 325 Assignment 2 Part 0
; Author:  Julia Zochodne
; Date:    February 20, 2011

;This program scans each line of the ASM68_CW.txt file and counts the number
;of labels contained in it. The number of labels is displayed, and the
;program is exited.
;----------------------------------------------------------------------------

[GLOBAL mystart]

extern _fopen                   ;Imports C-function for opening file.
extern _fclose                  ;Imports C-function for closing file.
extern _fgets                   ;Imports C-function for reading a string.
extern _printf                  ;Imports C-function for output to screen.

;----------------------------------------------------------------------------
[SECTION .text]
;----------------------------------------------------------------------------

;----------------------------------------------------------------------------
;The mystart section in this program opens the file specified in the .data
;section. It also returns a pointer to this file in a register.
;----------------------------------------------------------------------------

mystart:
        push dword modeR                ;Pushes read mode arg onto stack
        push dword filename             ;Pushes filename arg onto stack
        call _fopen                     ;Opens file using C function
        add esp, 8                      ;Realigns stack to prevent memory leaks
        cmp eax, 0                      ;Checks for errors
        je openError                    ;If returned 0 and an error occurred, jump
        mov [filePtr], eax              ;Assign returned file pointer to variable
        mov esi, 0                     ;Initialize label counting register

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
        inc esi                       ;Update the counter: if the above conditions were not met the line must have a label.
        jmp readLoop                  ;Jump so that next line can be read

;----------------------------------------------------------------------------
;The close section displays the number of labels counted and closes the file.
;The program then jumps to the final ret instruction.
;----------------------------------------------------------------------------

close:
        push esi                     ;Passes 2nd argument specifying the number of labels
        push dword labelCount        ;Passes 1st argument specifying the string format
        call _printf                 ;Calls C-function for output
        add esp, 8                   ;Realigns stack to prevent memory leaks
        push dword [filePtr]         ;Passes argument containing a pointer to the open file
        call _fclose                 ;Calls C-function to close the file
        add esp, 4                   ;Realigns stack to prevent memory leaks
        jmp end                      ;Jumps past input error section to reach end of program

;----------------------------------------------------------------------------
;The openError section prints out an error message if the specified
;file could not be opened successfully.
;----------------------------------------------------------------------------

openError:
        push dword openErrStr        ;Passes argument specifying the string format for an error message
        call _printf                 ;Calls C-function for output
        add esp, 4                   ;Realigns stack to prevent memory leaks


end:
        ret                          ;Ends program



;----------------------------------------------------------------------------
[SECTION .data]
;----------------------------------------------------------------------------

modeR db "r", 0                        ;Initializes a read-mode variable
filename db "ASM68_CW.txt",0           ;Specifies the name of the input file
openErrStr db "error opening file", 13, 10  ;Specifies IO error message.
labelCount db "%d", 13, 10             ;Specifies string for displaying count
;----------------------------------------------------------------------------
[SECTION .bss]
;----------------------------------------------------------------------------

inBuff resb 110                 ;Zero initializes an input buffer variable
filePtr resd 1                  ;Zero initializes a file pointer variable


