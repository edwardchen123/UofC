/**
 * CPSC 325 Assignment 3 - Conway's Game of Life
 * By Andrew Helwer and Julia Zochodne
 **/

#include <stdio.h>
#include <stdlib.h>
#include <pc.h>
#include <dpmi.h>
#include <go32.h>
#include <dos.h>

#ifndef USENEAR
#include <sys/farptr.h>
#else
#include <sys/nearptr.h>
#include <crt0.h>
int _crt0_startup_flags = _CRT0_FLAG_NEARPTR | _CRT0_FLAG_NONMOVE_SBRK;
#endif

#define BYTE unsigned char

/**
 * Encapsulates useful information (hence name). From VBETEST.C.
 **/
typedef struct {
  unsigned ModeAttributes;
  unsigned granularity,startseg,farfunc;
  short bscanline;
  short XResolution;
  short YResolution;
  short charpixels;
  unsigned bogus1,bogus2,bogus3,bogus4;
  unsigned PhysBasePtr;
  char bogus[228];
} ModeInfoBlock;

unsigned int ADDR;
int width,height;
int my_video_ds;

//Contstants governing position of stuff on screen
#define ROWS 32
#define COLUMNS 49
#define XOFF 23
#define YOFF 107
#define SQWID 20

//Constants defining colors of various things
#define WHITE 15
#define RED 4
#define PURPLE 5
#define GREEN 2
#define PINK 13
#define GREY 8
#define BLACK 0
#define GRIDCOLOR GREY
#define SQCOLOR PURPLE
#define BGCOLOR BLACK
#define SELCOLOR GREEN

/**
 * Initializes registers. Provided by VBETEST.C.
 **/
ModeInfoBlock *get_mode_info(int mode)
{
    static ModeInfoBlock info;
    __dpmi_regs r;

    /* Use the transfer buffer to store the results of VBE call */
    r.x.ax = 0x4F01;
    r.x.cx = mode;
    r.x.es = __tb / 16;
    r.x.di = 0;
    __dpmi_int(0x10, &r);
    if(r.h.ah) return 0;
    dosmemget(__tb, sizeof(ModeInfoBlock), &info);
    return &info;
}

/**
 * Switches graphics to XGA. Provided by VBETEST.C.
 **/
void init_graphics(void)
{
    __dpmi_meminfo info;
    __dpmi_regs reg;
    ModeInfoBlock *mb;

    mb=get_mode_info(0x4105);               //Gets XGA mode info

    if (!mb) {
	printf("Get XGA mode info failed.\n");
	exit(1);
    }
    if (!(mb->ModeAttributes & 0x80)) {
	printf("Linear frame buffer not supported for XGA mode.\n");
	exit(1);
    }

    width=mb->XResolution;
    height=mb->YResolution;
    info.size=width*height;
    info.address=mb->PhysBasePtr;
    if(__dpmi_physical_address_mapping(&info) == -1) {
	printf("Physical mapping of address 0x%x failed!\n",mb->PhysBasePtr);
	exit(2);
    }
    ADDR=info.address;		/* Updated by above call */
    printf("XGA mode 0x4105: %d x %d, linear frame: 0x%x\n",width,height,ADDR);
    delay(1500);
    reg.x.ax=0x4f02;                      //Sets ax for XGA
    reg.x.bx=0x4105;		     //Sets bx for XGA
    __dpmi_int(0x10,&reg);		/* set the mode */
    if(reg.h.al != 0x4f || reg.h.ah) {
	printf("Mode set failed!\n");
	exit(3);
    }
}

/**
 * Resets graphics display mode. Provided by VBETEST.C
 **/
void close_graphics(void)
{
    __dpmi_regs reg;
    reg.x.ax = 0x0003;
    __dpmi_int(0x10,&reg);
}

/**
 * Declares useful variables. From VBETEST.C.
 **/
volatile char raw_key =0;
_go32_dpmi_seginfo old_handler, new_handler;

/**
 * Takes input from keyboard; output acknowledges input. From VBETEST.C.
 **/
void NewInt9(void){
    raw_key = inportb(0x60);
    outportb(0x20,0x20);
}

/**
 * Used for calculation of size of code so it may be locked in memory. From 
 * VBETEST.C.
 **/
void LOCKint9(){}

/**
 * key_init
 * This function initiates the interrupt for the keyboard. This was given 
 * as part of the VBETEST.C example.
 **/
void key_init(void){
//Finds location of old handler
        _go32_dpmi_lock_code(NewInt9, (unsigned long)(LOCKint9 - NewInt9));
        _go32_dpmi_get_protected_mode_interrupt_vector(9, &old_handler);
//Set new handler
        new_handler.pm_offset = (unsigned long)NewInt9;
        new_handler.pm_selector = _go32_my_cs();
//Set in protected mode
        _go32_dpmi_allocate_iret_wrapper(&new_handler);
        _go32_dpmi_set_protected_mode_interrupt_vector(9, &new_handler);
}

/**
 * key_delete
 * This function returns keyboard control to the operating system. This was 
 * given as part of the VBETEST.C example.
 **/
void key_delete(void){
        _go32_dpmi_set_protected_mode_interrupt_vector(9, &old_handler);
        _go32_dpmi_free_iret_wrapper(&new_handler);
}

/**
 * waitVRetrace
 * This function causes the system to wait until the rising edge of the clock
 * signal to draw to the screen. This was given as part of the VBETEST.C 
 * example.
 **/
void waitVRetrace(){
        while(inportb(0x3da) & 8);
        while(!inportb(0x3da) & 8);
}

/**
 * drawPixel
 * This function draws a single pixel of specified color at an (x,y) location
 * on-screen. This was given as part of the VBETEST.C example.
 **/
void drawPixel(int x, int y, char color){
    _farpokeb(my_video_ds, (y*width)+x, color);
}

/**
 * drawVLine
 * This function draws a vertical line starting at a certain location and
 * continuing for a specified length in a specified color.
 **/
void drawVLine(int x, int y, int length, char color){
    int i;
    for (i=0; i<length; i++) {
	drawPixel(x,y+i,color);
    }
}

/**
 * drawHLine
 * This function draws a horizontal line starting at a certain location and
 * continuing for a specified length in a specified color.
 **/
void drawHLine(int x, int y, int length, char color){
    int i;
    for (i=0; i<length; i++){
	drawPixel(x+i,y,color);
    }
}

/**
 * Draws a square at the specified location and with the specified color using
 * global constants for offsets and such.
 **/
void drawSquare(int x, int y, char color) {
    x = x*SQWID+XOFF+1;
    y = y*SQWID+YOFF+1;
    int i;
    for (i=1; i<SQWID-2; i++) {
	drawHLine(x+1,y+i,SQWID-3,color);
    }
}

/**
 * updateSquare
 * This function checks a position in the 2D array and decides if a square
 * needs to be drawn, and if so what color.
 **/
void updateSquare(int model[][ROWS], int x, int y) {
    int state = model[x][y];
    if (state==0) {
	drawSquare(x,y,BGCOLOR);
	return;
    }
    if (state==1) {
	drawSquare(x,y,SQCOLOR);
	return;
    }
}

/**
 * check
 * Checks all 8 neighbors of a square and returns 0 if the square remains dead, 
 * 1 if the square survives, and 2 if the square dies.
 **/
int check(int model[][ROWS], int x, int y) {
    int isAlive = model[x][y];
    int neighbors = 0;
    if (x>0 && y>0 && model[x-1][y-1]>=1) {neighbors++;} //NW
    if (x>0 && model[x-1][y]>=1) {neighbors++;} //W
    if (x>0 && y<ROWS-1 && model[x-1][y+1]>=1) {neighbors++;} //SW
    if (y<ROWS-1 && model[x][y+1]>=1) {neighbors++;} //S
    if (x<COLUMNS-1 && y<ROWS-1 && model[x+1][y+1]>=1) {neighbors++;} //SE
    if (x<COLUMNS-1 && model[x+1][y]>=1) {neighbors++;} //E
    if (x<COLUMNS-1 && y>0 && model[x+1][y-1]>=1) {neighbors++;} //NE
    if (y>0 && model[x][y-1]>=1) {neighbors++;} //N

    if (isAlive && neighbors<2) {return 2;} //dies from loneliness
    if (isAlive && neighbors<4) {return 1;} //survives	
    if (isAlive) {return 2;} //dies from overcrowding
    if (neighbors==3) {return -1;} //cell is born
    return 0; //nothing happens
}

/**
 * updateModel
 * Simulates one generation of Conway's Game of Life on the 2D array. This must 
 * be done in two steps, so that a change in an area of the array checked first 
 * does not affect areas of the array checked later. Additionally, only 
 * necessary changes are made as far as drawing elements on the screen, vastly
 * improving efficiency over a batch redraw of model.
 **/
void updateModel(int model[][ROWS]) {
    // Iterates through array and sets future state in each location
    int x;
    int y;
    for (x=0; x<COLUMNS; x++) {
	for (y=0; y<ROWS; y++) {
	    int state = check(model, x, y);
	    if (state==2) {drawSquare(x,y,BGCOLOR);}
	    if (state==-1) {drawSquare(x,y,SQCOLOR);}
	    model[x][y] = state;
	}
    }
    // Iterates through array and updates future state to actual state
    for (x=0; x<COLUMNS; x++) {
	for (y=0; y<ROWS; y++) {
            int state = model[x][y];
	    //translates 'future' states into current state (1 or 0)
            if (state==0 || state==2) {model[x][y]=0;}
            else {model[x][y]=1;}
	}
    }
}

/**
 * drawGrid
 * Draws a grid on the screen according to global constants in the specified
 * color.
 **/
void drawGrid(char color){
    int i;
    for (i=0; i<COLUMNS+1; i++) //draws all vertical lines
	drawVLine(XOFF+(i*SQWID), YOFF, ROWS*SQWID, color);
    for (i=0; i<ROWS+1; i++) //draws all horizontal lines
	drawHLine(XOFF, YOFF+(i*SQWID), COLUMNS*SQWID, color);
}

/**
 * drawText
 * Draws the specified string at the specified position in the specified color
 * using text blitting.
 **/
void drawText(int x, int y, char text[],char color) {
    // defines segment for text
    int mytext = __dpmi_allocate_ldt_descriptors(1);
    __dpmi_set_segment_base_address(mytext, 0xffa6e);
    __dpmi_set_segment_limit(mytext, 8*128);
    char row;
    int i,j,k;
    for (i=0; text[i]!=0; i++) { //iterates through string
	for (j=0; j<8; j++) { //iterates through rows
	    row = _farpeekb(mytext, text[i]*8+j); //gets jth row of char
	    for (k=0; k<8; k++) { //iterates through pixels
		// decides if pixel should be drawn at position
		if (row & 0x80) {drawPixel(x+k+8*i,y+j,color);}
		row <<= 1; //shifts bits to left by one
	    }
	}
    }
}

/**
 * printHeader
 * Defines a selection of messages and instructions then prints them onto the
 * screen at defined positions and in defined colors.
 **/
void printHeader() {
        char title[] = "Conway's Game of Life";
        char authors[] = "By Andrew Helwer and Julia Zochodne";
        char insLabel[] = "KEYBOARD CONTROLS:";
        char enter[] = "Press enter to pause/run simulation";
        char arrows[] = "Use arrow keys to move cursor";
        char space[] = "Press spacebar to add/remove block";
        char esc[] = "Press ESC to quit";
        drawText(XOFF,XOFF,title,WHITE);
        drawText(XOFF,XOFF+10,authors,WHITE);
        drawText(700,XOFF,insLabel,WHITE);
        drawText(700,XOFF+10,enter,WHITE);
        drawText(700,XOFF+20,arrows,WHITE);
        drawText(700,XOFF+30,space,WHITE);
        drawText(700,XOFF+40,esc,WHITE);
}

/**
 * run
 * Run is the main control body of the program. It first prints the header which
 * contains directions on how to use the program then enters the paused state
 * in which the user may edit the population using a cursor controlled by the 
 * arrow keys. If the user presses Enter, the simulation is started and will
 * continue until the user again presses Enter to pause or Esc to exit.
 **/
void run(int model[][ROWS]) {
    printHeader(); //prints directions
    int i = COLUMNS/2; //initializes position of cursor to middle of screen
    int j = ROWS/2;
    raw_key = 0x1C; //sets raw_key = ENTER
    while (raw_key != 0x01) { //loops until user hits Esc
	if (raw_key==0x1C) { //checks for enter pressed
            raw_key=0;
	    waitVRetrace();
	    drawGrid(GRIDCOLOR); //draws grid on screen for edit mode
	    drawSquare(i,j,SELCOLOR); //draws cursor at position on screen
	    while (raw_key!=0x1C && raw_key!=0x01) { //loops until Esc or Enter
		waitVRetrace();
		if (raw_key==0x48) { //up arrow has been pressed
		    raw_key = 0;
		    updateSquare(model,i,j); //erases cursor
		    if (j>0) {j--;} //updates cursor position
		    drawSquare(i,j,SELCOLOR); //draws cursor at new position
		}
		if (raw_key==0x50) { //down arrow has been pressed
		    raw_key = 0;
		    updateSquare(model,i,j);
		    if (j<ROWS-1) {j++;}
		    drawSquare(i,j,SELCOLOR);
		}
		if (raw_key==0x4B) { //left arrow has been pressed
		    raw_key = 0;
		    updateSquare(model,i,j);
		    if (i>0) {i--;}
		    drawSquare(i,j,SELCOLOR);
		}
		if (raw_key==0x4D) { //right arrow has been pressed
		    raw_key = 0;
		    updateSquare(model,i,j);
		    if (i<COLUMNS-1) {i++;}
		    drawSquare(i,j,SELCOLOR);
		}
		if (raw_key==0x39) { //spacebar has been pressed
		    raw_key = 0;
		    //flips the life state of current square
		    if (model[i][j]==1) {model[i][j]=0;}
		    else {model[i][j]=1;}
		}
	    }
            if (raw_key==0x1C) {raw_key=0;} //clears raw key if enter was pressed
	    updateSquare(model,i,j); //erases cursor
	    drawGrid(BGCOLOR); //erases grid for simulation mode
	    while(raw_key!=0x01 && raw_key!=0x1C) { //loops until Esc or Enter
		waitVRetrace();
		updateModel(model); //simulates game of life and redraws squares
		delay(75);
	    }
	}
    }
}

/**
 * populate
 * Takes a 2D array and a file pointer for a file containing a description of
 * an initial state, then populates the array accordingly in addition to drawing
 * the initial state on the screen.
 **/
void populate(int model[][ROWS], FILE * fp) {
    int x;
    int y;
    while (fscanf(fp, "%d %d", &x, &y) != EOF) {
	model[x][y] = 1;
        drawSquare(x,y,SQCOLOR);
    }
}

/**
 * main
 * This function initializes all graphical variables as well as opens the input
 * file and calls a function which populates a 2D array accordingly. It then
 * calls run(), which takes care of the actual running of the Game of Life.
 * Finally, it restores the video mode to the original state and releases
 * keyboard control.
 **/
int main(int argc, char *argv[]) {
    int model[COLUMNS][ROWS] = {0}; //this array stores the current life state
    key_init(); //takes control of keyboard input
    init_graphics(); //switches to XGA
    my_video_ds = __dpmi_allocate_ldt_descriptors(1);
    __dpmi_set_segment_base_address(my_video_ds,ADDR);
    ADDR = 0;	/* basis now zero from selector */
    __dpmi_set_segment_limit(my_video_ds,(width*height)|0xfff);
    //the following reads in a file if one was given and populates the grid
    if (argc==2) {
	FILE *fp = fopen(argv[1], "r");
	if (fp == 0) {
            printf("Error - invalid filename\n" );
	    return 0;
        }
        else {
	    populate(model, fp);
	}
    }
    run(model); //runs the program
    close_graphics(); //restores video mode to original state
    key_delete(); //releases control over the keyboard
    return 0;
}
