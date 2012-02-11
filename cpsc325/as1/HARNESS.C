//----------------------------------------------------------------------------
// CPSC 355 - Protected Mode Assembly Program Harness
// Author:  Sonny Chan
// Date:    April 29, 2001
//
// Uses DJGPP/DPMI to provide a protected mode interface the user's assembly
// language program.
//----------------------------------------------------------------------------

#include <go32.h>

// function contained within the assembly language module, portal.asm
extern void go(unsigned long selector, unsigned long buffer);

int main()
{
   go(_dos_ds, __tb);   // call the ASM module with real mode addressing info
   
   return 0;
}
