#include <stdio.h>

#define BUFF_SIZE 200

/**
 * CircularBuffer
 * This structure consists of an array of chars, as well as head and tail pointers to keep track of the beginning and end
 * of occupied data. A size variable is included to keep track of the current size of the buffer.  
 **/
typedef struct {
	char buffer[BUFF_SIZE];		//array for buffer
	char* head;			//points to beginning of data
	char* tail; 			//points to end of data
	int size; 			//size of buffer
} CircularBuffer; 

/**
 * insert
 * This function adds an element to the rear of the circular buffer. It also updates the tail position and buffer size.
 * If the tail is pointing to the end of the buffer, it wraps around so that it is pointing to the beginning. If the buffer
 * is empty, tail position remains constant.  The function takes a pointer to a circular buffer, and the element to be 
 * inserted as parameters. 
 **/
int insert(CircularBuffer* c, char elem){ 
	if ((c->size) < 200) {
		//Assigns elem to appropriate position in the array			
		*(c->tail)=elem;
		//Updates the current size of the buffer
		(c->size)++;
		//Adjusts tail pointer 
		(c->tail)++;
		//Checks whether tail pointing to the end of the buffer
		if (c->tail == (c->buffer)+BUFF_SIZE) {
			//wraps around to beginning
			c->tail = (c->buffer);
		}
		return elem;
	}
	else {
		return 0;	
	}
}

/**
 * getElem
 * This function removes the first element from the front of the buffer. This element is returned; the position of the head
 * pointer and the size of the buffer are adjusted accordingly. 
 **/
char getElem(CircularBuffer* c) {
	if ((c->size) > 0) {
		//Sets local variable to current first array element 			
		char elem = *(c->head); 
		//Updates the current size of the buffer
		(c->size)--;						
		//Points head to the next element in the array 
		(c->head)++;	
		//Checks whether tail pointing to the end of the buffer
		if (c->head == (c->buffer)+BUFF_SIZE) {
			//wraps around to beginning
			c->head = (c->buffer);
		}						
		//Returns element
		return elem;
	}
	else {
		return 0;
	}	
}
