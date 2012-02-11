
#include <stdio.h>
#include "cbuff.c" 

#define PACKET_SIZE 3
#define BGCOLOR  0 
#define SQCOLOR 1


typedef struct
{
	int pressed;			//Checks if mouse is pressed
	int x; 					//x position
	int y; 					//y position
} Position;

/**
 * track
 * This function reads a mouse packet from a circular buffer and updates the position of the cursor on the screen.  
 **/

void track(Position* p, CircularBuffer* c){

	int i, xmove, ymove;								
	
	for (i=0; i<PACKET_SIZE; i++){							//Read 3 bytes from the circular buffer 
		
		if (c->size !=0){										//Reads if buffer contains at least one element
			char byte = getElem(c);
			if (i==1){	
				xmove = (int)byte;							//Gets 2nd byte, which records x movement
			}
			if (i==2){											//Gets 3rd byte, which records y movement
				ymove = (int)byte; 
			}
		
		}
		else {
			xmove=0; 
			ymove=0; 
		}
	}
	 
	//drawSquare(((p->x),(p->y),BGCOLOR);							//Erase current cursor position
	while ((xmove !=0) || (ymove !=0)){							//Checks that mouse is currently moving
		if ((xmove & 0x10) == 0x10){							//Checks whether X sign bit (bit 4) is set 
			p->x = (p->x)--; 										//If so, update x position left 	
		}
		else {
			p->x = (p->x)++;										//Otherwise, update x position right 
		}

		if ((ymove & 0x20) == 0x20){							//Checks whether Y sign bit (bit 5) is set
			p->y = (p->y)--;										//If so, move y position down 
		}
		else {
			p->y = (p->y)++;										//Otherwise, update y position up				
		}

	}
	//drawSquare(((p->x),(p->y),BGCOLOR);							//Erase current cursor position
}


int main(){

return 0; 

}


