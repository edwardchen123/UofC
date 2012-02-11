/* Tiny mouse demo. Displays raw mouse codes. Exit with right mouse button.
Mostly organized by C. R. Walpole. PS/2 mouse

With thanks to the first group to show mouse operation  on a 455 PC assignment, June, 1999
    Shaun Laing,  Adrian Gareau and Louis Lee 

*/

#include <stdio.h>  /*NOTE!! We can't use getkey() or anything like that
		after we install the interrupt!! Why can't you try debugging an ISR           		
		by inserting a printf ("Entering ISR.\n")  ?CW */
#include <go32.h>
#include <dpmi.h>

volatile char raw_code=0;
/*The volatile attribute forces gcc to access the actual variable raw_code
rather than a cached copy */


_go32_dpmi_seginfo old_handler,new_mousehandler;
/* Contains the selector/offset pairs for the far pointers of the old and new
handlers.*/


/*******************************************************************************/
void NewInt74(void)
/* The actual ISR called asynchronously when the mouse is moved or clicked.*/

{
	raw_code = inportb(0x60); /*PS/2 data port address */

	outportb(0xA0, 0x20);   /*This resets the S PIC. CW*/
        outportb(0x20, 0x20);   /*Reset M PIC */
}


void LOCKint74() { }
/* A dummy function declaration used to obtain the length of the ISR function.
The function can be locked when we have this value for the length. */

/***************************************************************************/
void mouse_init(void)     /*Note that we are doing NO device initialization. 
			We are assuming that this was done suitably when the system 
			was booted and the standard mouse driver was installed. THis is very poor
			programming practice! CW */
{

 /*  _go32_dpmi_lock_data(mousequeue,200);   I'm not using these in this simple
			demo so they are commented out. CW

   _go32_dpmi_lock_data(&mousequeue,sizeof(mousequeue));*/

   _go32_dpmi_lock_code(NewInt74,(unsigned long)(LOCKint74-NewInt74));

// This ensures that the handler is not placed into paged memory.

   _go32_dpmi_get_protected_mode_interrupt_vector(0x74,&old_handler);
   /* Reads the selector and offset of the old handler into a structure, so
   that it can be restored later. */

   /*Obtains the far pointer for the mouse ISR */

   new_mousehandler.pm_selector=_go32_my_cs();  /* Note that _go32_my_cs()
 				returns the	selector for the program's code.*/

   new_mousehandler.pm_offset=(unsigned long)NewInt74;

   _go32_dpmi_allocate_iret_wrapper(&new_mousehandler);
   /* The wrapper saves the machine state using a series of PUSH instructions. */

   _go32_dpmi_set_protected_mode_interrupt_vector(0x74,&new_mousehandler);

   /* Places the ISR far pointer into a protected mode interrupt table. */

}

/***************************************************************************/

/**
 * mouse_delete
 * This function returns us to the old handler's address we saved before.
 **/
void mouse_delete(void)

{
   _go32_dpmi_set_protected_mode_interrupt_vector(0x74,&old_handler);
   _go32_dpmi_free_iret_wrapper(&new_mousehandler);   
}

/***************************************************************************/

int main()
{


  char temp = 0;
  long temp2 = 0;
  printf("This program will display raw mouse codes.\n");
  printf("To exit hit the left mouse button.\n\n");
  printf("Grabbing mouse interrupt...\n");
  mouse_init();

  do{
  do {} while (temp==raw_code);   /*Isn't this weird! Raw_code doesn't get assigned
			by main or any function of main!  */

  temp2=(long)raw_code;
  //temp2 &= 0xFF;
  temp2 &= 0xFF;
  cprintf ("%2X",temp2);
  cprintf ("  ");

  temp = raw_code;
  }
  while (temp != 0x09);     /*left mouse button*/
    

  mouse_delete();
  printf ("\nWe have released the mouse interrupt.\n");
  printf ("Strike any key to quit.\n");
  getkey(); 

  return 0;
}

/*
This demo program has a small bug which doesn't prevent it from working fine, except that
the mouse packet is 3 bytes. However, this program will
occasionally seem to indicate the presence of 2-byte packets. Why? CW 
*/