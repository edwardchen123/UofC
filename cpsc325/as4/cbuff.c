
#include <stdio.h>


#define BUFF_SIZE 200
#define ELEM_NUM  (BUFF_SIZE-1)

/**

 * CircularBuffer

 * This structure consists of an array of chars, as well as head and tail pointers to keep track of the beginning and end
 * of occupied data. A size variable is included to keep track of the current size of the buffer.  

 **/


typedef struct
{
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

void insert(CircularBuffer* c, char elem){ 
	if (c->tail == (c->buffer)+ELEM_NUM-1){			//Checks whether tail pointing to the end of the buffer
		c->tail = (c->buffer)-1;}				//If so, wraps around to beginning
	if (c->size != 0){					//Adjusts tail pointer so points past the last char
		(c->tail)++;
	} 
				
	*(c->tail)=elem; 				//Assigns elem to appropriate position in the array
	(c->size)++;					//Updates the current size of the buffer
}


/**

 * getElem

 * This function removes the first element from the front of the buffer. This element is returned; the position of the head
 * pointer and the size of the buffer are adjusted accordingly. 

 **/

char getElem(CircularBuffer* c){
	char elem; 
	elem = *(c->head); 			//Sets local variable to current first array element 			
	(c->head)++;						//Points head to the next element in the array 
	(c->size)--;						//Updates the current size of the buffer
	return elem; 					//Returns first element
}

/*int main()
{
	CircularBuffer cbuff; 
	cbuff.size = 0;				        //Initializes buffer size 
	cbuff.head = cbuff.buffer; 					//Sets head pointer to base array address
	cbuff.tail = cbuff.buffer; 					//Sets tail point to base array address

	int i; 
	printf("Current Buffer Size: %d \n", cbuff.size); 

	for (i=0; i<BUFF_SIZE; i++){
		insert(&cbuff, '0'); 
		printf("Current Tail Position: %d \n", cbuff.tail); 
		printf("%c, \n", cbuff.buffer[i]); 	 
		}
		
	printf("Current Buffer Size: %d \n", cbuff.size); 
	return 0; 
}*/ 

