/**********************************************
Last Name: Helwer
First Name: Andrew
Student ID: 10023875
Course: CPSC 457
Tutorial Section: T05 Assignment: 2
Question: 2
File name: pc.cpp
**********************************************/

#include <iostream>
#include <vector>
#include <utility>
#include <pthread.h>
#include <stdlib.h>

#include "circular_buffer.hpp"
#include "task.hpp"
#include "con_comm.hpp"

/* *
 * Function Dispatch
 *
 * Called on thread launch. Dispatches thread to appropriate Execute() function
 * in class implementing Task interface.
 * */
void* Dispatch(void* varg) {
	Task* t = static_cast<Task*>(varg);
	t->Execute();
}

int main(int argc, char* argv[]) {	
    // Checks for correct program usage
    if (argc != 6) {
        std::cout << "Incorrect program usage." << std::endl;
        return 0;
    }
    // Parses command line arguments
	unsigned long buffSize = strtoul(argv[1], NULL, 10); 
    unsigned long buffCount = strtoul(argv[2], NULL, 10);
    unsigned long prodCount = strtoul(argv[3], NULL, 10);
    unsigned long consCount = strtoul(argv[4], NULL, 10);
    unsigned long itemCount = strtoul(argv[5], NULL, 10);

	// Creates concurrent communicator
	ConComm comm;

	// Creates circular buffers
	std::vector<CircularBuffer*> buffers;
	for (unsigned long i = 0; i < buffCount; ++i)
		buffers.push_back(new CircularBuffer(buffSize, i, comm));

	// Creates and launches producers
	std::vector< std::pair<Producer*, pthread_t> > producers;
	for (unsigned long i = 0; i < prodCount; ++i) {
		Producer* p = new Producer(buffers, comm, i, itemCount);
		pthread_t threadId = 0;
		int error = pthread_create(&threadId, NULL, Dispatch, static_cast<void*>(p));
		producers.push_back(std::pair<Producer*, pthread_t>(p, threadId));
	}

	// Creates and launches consumers
	std::vector< std::pair<Consumer*, pthread_t> > consumers;
	for (unsigned long i = 0; i < consCount; ++i) {
		Consumer* c = new Consumer(buffers, comm, i);
		pthread_t threadId = 0;
		int error = pthread_create(&threadId, NULL, Dispatch, static_cast<void*>(c));
		consumers.push_back(std::pair<Consumer*, pthread_t>(c, threadId));
	}
	
    // Waits for producers to terminate, then marks as finished
    for (unsigned long i = 0; i < producers.size(); ++i)
        pthread_join(producers[i].second, NULL);
	comm.MarkAsFinished();

	// Waits for consumers to terminate
    for (unsigned long i = 0; i < consumers.size(); ++i)
        pthread_join(consumers[i].second, NULL);
	
    // Cleanup
	for (unsigned long i = 0; i < buffers.size(); ++i)
		delete buffers[i];
	for (unsigned long i = 0; i < producers.size(); ++i)
		delete producers[i].first;
	for (unsigned long i = 0; i < consumers.size(); ++i)
		delete consumers[i].first;

    std::cout << "Consumed " << itemCount*prodCount << " items of " 
                << itemCount*prodCount << " produced items." << std::endl;

	return 0;
}

