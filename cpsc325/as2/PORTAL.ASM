;----------------------------------------------------------------------------
; CPSC 355 - DOS INT 21H Function Cascade Interrupt Handler
;
; Author:  Sonny Chan
; Date:    April 29, 2001
;
; Provides protected/real mode translation for the following functions:
;   09 - write string to standard output
;   0A - buffered input
;   3C - create or truncate file
;   3D - open existing file
;   3F - read from file or device
;   40 - write to file or device
;
; All other functions which do not require input/output buffers and pointer
; addressing (eg. 02 - write character) will function normally as expected.
;----------------------------------------------------------------------------

[GLOBAL _go]                      ; function called from C program
[EXTERN mystart]                  ; function in user ASM source

;----------------------------------------------------------------------------
[SECTION .text]
;----------------------------------------------------------------------------

;----------------------------------------------------------------------------
; Installs the DOS INT 21H cascade into INT 0F1H for application use.
; Then calls the user's mystart function in another ASM module.
; To be called from the C module's main() function.

_go:
         enter 0, 0
         pushad

         mov  eax, [ebp+8]        ; store DOS selector
         mov  [dosds], ax
         mov  eax, [ebp+12]       ; store 4K buffer address
         mov  [dostb], eax
         mov  edx, eax            ; copy transfer buffer address
         shr  eax, 4              ; calculate buffer's segment
         mov  [dosseg], ax        ; store real mode buffer segment
         and  edx, 0fh            ; calculate buffer's offset
         mov  [dosoff], dx        ; store real mode buffer offset

         call lock_mem            ; call function to lock ISR's code
         call install_handler     ; call function to put handler at INT 0F1H

         pushad                   ; save all registers
         call mystart             ; and call user's program
         popad                    ; restore all registers

         call remove_handler      ; clean up the INT 0F1H handler

         popad
         leave
         ret


;----------------------------------------------------------------------------
; Function to lock the portal handler's code so that it won't be paged out.

lock_mem:
         mov   ax, 0006h          ; select DPMI function get segment base
         mov   bx, cs             ; of CS
         int   31h
         shl   ecx, 16
         mov   cx, dx             ; then use it to lock our mouse handler code
         add   ecx, portal_handler
         mov   ebx, ecx
         shr   ebx, 16
         mov   esi, HSIZE
         mov   edi, esi
         shr   esi, 16
         mov   ax, 0600h          ; with DPMI function lock linear region
         int   31h

         ret

;----------------------------------------------------------------------------
; Function to install the portal handler into INT 0F1H, saving the old
; handler's address.

install_handler:
         mov   ax, 204h           ; use DPMI function get interrupt vector
         mov   bl, 0f1h           ; for INT 0F1H, available for application
         int   31h
         mov   [oldhsel], cx      ; store the old handler's selector
         mov   [oldhoff], edx     ; and offset

         mov   ax, 205h           ; use DPMI function set interrupt vector
         mov   bl, 0f1h           ; for INT 0F1H
         mov   cx, cs             ; put our CS as selector
         mov   edx, portal_handler; and our handler's offset
         int   31h

         ret

;----------------------------------------------------------------------------
; Function that restores the old INT 0F1H handler's address into the IVT.

remove_handler:
         mov   ax, 205h           ; use DPMI function set interrupt vector
         mov   bl, 0f1h           ; for INT 0F1H
         mov   cx, [oldhsel]      ; putting the old handler's selector
         mov   edx, [oldhoff]     ; and offset back
         int   31h

         ret

;----------------------------------------------------------------------------
; Macro that searches for the first occurance of the parameter in a buffer
; at DS:EDX, then copies all characters before that into the transfer buffer
; For use with the portal int 0f1h handler

%macro SearchMove 1
         mov  edi, edx            ; put offset into EDI
         mov  al, %1              ; put target byte into AL
	 mov  ecx, -1
repne    scasb                    ; search for it
         mov  ecx, 1              ; calculate number of bytes to copy in ECX
         add  ecx, edi
         sub  ecx, edx
         mov  esi, edx            ; put buffer offset into ESI
         mov  edi, [cs:dostb]     ; put transfer buffer addres into ES:EDI
         mov  ax, [cs:dosds]
         mov  es, ax
rep      movsb                    ; copy bytes
%endmacro

;----------------------------------------------------------------------------
; Macro that copies the real mode address of the transfer buffer into DS:DX
; of the register call structure for real mode interrupt simulation.
; For use with the portal int 0f1h handler

%macro StoreDSDX 0
         mov  ax, [cs:dosseg]
         mov  [ebp-regs_size+regs.ds], ax
         movzx eax, word [cs:dosoff]
         mov  [ebp-regs_size+regs.edx], eax
%endmacro

;----------------------------------------------------------------------------
; Interrupt handler which does all the protected to real mode address
; translations required, then cascades to DOS INT 21H functions.
; To be hooked onto INT 0F1H, which is reserved for application use.

portal_handler:
         enter regs_size, 0
         push edi
         push esi
         push ds
         push es

         mov  [ebp-regs_size+regs.edi], edi
         mov  [ebp-regs_size+regs.esi], esi
         mov  [ebp-regs_size+regs.ebx], ebx
         mov  [ebp-regs_size+regs.edx], edx
         mov  [ebp-regs_size+regs.ecx], ecx
         mov  [ebp-regs_size+regs.eax], eax
         mov  word [ebp-regs_size+regs.sp], 0
         mov  word [ebp-regs_size+regs.ss], 0

         cld                      ; clear direction flag for moves
         push eax                 ; save function number
         push ecx                 ; save incoming count
         push edx                 ; save incoming offset

         cmp  ah, 09h             ; test for write string function 09H
         jne  .skip09
         SearchMove '$'           ; search for the terminating $
         StoreDSDX
         jmp  .go_int

.skip09:
         cmp  ah, 0ah             ; test for buffered input function 0AH
         jne  .skip0a
         mov  esi, edx            ; copy the first word of the buffer
         mov  edi, [cs:dostb]
         mov  ax, [cs:dosds]
         mov  es, ax
         movsw
         StoreDSDX
         jmp  .go_int

.skip0a:
         cmp  ah, 3dh             ; test for create file function 3DH
         je   .do3cd
         cmp  ah, 3ch             ; test for open file function 3CH
         jne  .skip3cd
.do3cd:
         SearchMove 0             ; search for the null terminator of fname
         StoreDSDX
         jmp  .go_int

.skip3cd:
         cmp  ah, 3fh             ; test for read from file function 3FH
         jne  .skip3f
         StoreDSDX
         jmp  .go_int

.skip3f:
         cmp  ah, 40h             ; test for write to file function 40H
         jne  .skip40
         movzx ecx, cx            ; copy CX number of bytes into buffer
         mov  esi, edx
         mov  edi, [cs:dostb]
         mov  ax, [cs:dosds]
         mov  es, ax
rep      movsb
         StoreDSDX
         jmp  .go_int

.skip40:

.go_int:
         mov  ax, [ebp+12]        ; copy the flags register into call struct
         mov  [ebp-regs_size+regs.flags], ax

         mov  ax, ss              ; set up ES with SS, since structure is
         mov  es, ax              ; on stack frame

         mov  ax, 0300h           ; select DMPI function: simulate interrupt
         mov  bx, 0021h           ; cascade to DOS INT 21H
         mov  cx, 0
         lea  edi, [ebp-regs_size]; offset of the register call structure
         int  31h

         pop  edx                 ; restore PM buffer offset
         pop  ecx                 ; restore incoming count
         pop  eax                 ; restore function number

         cmp  ah, 0ah             ; test for buffered input function 0AH
         jne  .skipd0a
         movzx ecx, byte [edx]    ; grab buffer size
         add  ecx, 2              ; add buffer header
         mov  ax, ds              ; load ES:EDI with PM buffer address
         mov  es, ax
         mov  edi, edx
         mov  ax, [cs:dosds]      ; load DS:ESI with transfer buffer address
         mov  ds, ax
         mov  esi, [cs:dostb]
rep      movsb                    ; copy input bytes back into PM buffer
         jmp  .done

.skipd0a:
         cmp  ah, 3fh             ; test for file read function 3FH
         jne  .skipd3f
         movzx ecx, cx            ; load ECX with buffer size
         mov  ax, ds              ; load ES:EDI with PM buffer address
         mov  es, ax
         mov  edi, edx
         mov  ax, [cs:dosds]      ; load DS:ESI with transfer buffer address
         mov  ds, ax
         mov  esi, [cs:dostb]
rep      movsb                    ; copy the read characters to PM buffer
         jmp  .done

.skipd3f:

.done:
         mov  ax, [ebp-regs_size+regs.flags]
         mov  [ebp+12], ax        ; update the save flags register

         mov  ebx, [ebp-regs_size+regs.ebx]
         mov  edx, [ebp-regs_size+regs.edx]
         mov  ecx, [ebp-regs_size+regs.ecx]
         mov  eax, [ebp-regs_size+regs.eax]

         pop  es
         pop  ds
         pop  esi
         pop  edi
         leave
         iret

dosds    dw   0                   ; selector for first megabyte
dostb    dd   0                   ; offset of 4K transfer buffer
dosseg   dw   0                   ; real mode segment of transfer buffer
dosoff   dw   0                   ; real mode offset of transfer buffer

end_portal:                       ; marks end of portal interrupt handler
         nop

;----------------------------------------------------------------------------
[SECTION .data]
;----------------------------------------------------------------------------

         struc regs               ; DPMI interrupt register call structure
.edi     resd 1
.esi     resd 1
.ebp     resd 1
.reservd resd 1
.ebx     resd 1
.edx     resd 1
.ecx     resd 1
.eax     resd 1
.flags   resw 1
.es      resw 1
.ds      resw 1
.fs      resw 1
.gs      resw 1
.ip      resw 1
.cs      resw 1
.sp      resw 1
.ss      resw 1
.padding resw 1
         endstruc

HSIZE    equ  end_portal - portal_handler

;----------------------------------------------------------------------------
[SECTION .bss]
;----------------------------------------------------------------------------

oldhoff  resd 1                   ; old INT 0F1H handler's offset
oldhsel  resw 1                   ; old INT 0F1H handler's selector
