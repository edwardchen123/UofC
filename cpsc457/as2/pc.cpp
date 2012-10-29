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
#include <time.h>

/* *
 * Class Task
 *
 * This pure virtual class is used in conjunction with the Dispatch function to
 * neatly extend pthread's thread launching capabilities, which are still stuck
 * firmly in the 80's.
 * */
class Task {
public:
	virtual void Execute() = 0;
	virtual void PrintStatus() const = 0;
	virtual void PrintExit() const = 0;
};

/* *
 * Function Dispatch
 *
 * Used in conjunction with pure virtual class Task to provide a facade of the
 * object-oriented paradigm to pthread thread launching. This is the function
 * passed to pthread_create().
 * */
void* Dispatch(void* varg) {
	Task* t = static_cast<Task*>(varg);
	t->Execute();
}

/* *
 * Class ConComm - the Concurrent Communicator
 *
 * This class encapsulates a number of constructs for synchronizing global
 * state and sending signals between threads. May as well make mutexes as
 * tolerable as possible if we aren't using the message-passing paradigm.
 * */
class ConComm {
public:
	ConComm() { 
		pthread_mutex_init(&m_outputMutex, NULL);
		pthread_mutex_init(&m_finishedMutex, NULL);
		pthread_mutex_init(&m_flightMutex, NULL);
		m_finished = false;	
		m_inFlight = 0;
	}
	~ConComm() {
		pthread_mutex_destroy(&m_outputMutex);
		pthread_mutex_destroy(&m_finishedMutex);
		pthread_mutex_destroy(&m_flightMutex);
	}
	// Acquires a lock on output before printing task status.
	void PrintOut(Task const& out) {
		pthread_mutex_lock(&m_outputMutex);
		out.PrintStatus();
		pthread_mutex_unlock(&m_outputMutex);
	}
	// Acquires a lock on output before printing task exit.
	void PrintExit(Task const& out) {
		pthread_mutex_lock(&m_outputMutex);
		out.PrintExit();
		pthread_mutex_unlock(&m_outputMutex);
	}
	void IncrementInFlight() {
		pthread_mutex_lock(&m_flightMutex);
		++m_inFlight;
		pthread_mutex_unlock(&m_flightMutex);
	}
	void DecrementInFlight() {
		pthread_mutex_lock(&m_flightMutex);
		--m_inFlight;
		pthread_mutex_unlock(&m_flightMutex);
	}
	// Checks whether all producers have reached their quotas.
	bool IsFinished() {
		bool finished = false;
		pthread_mutex_lock(&m_finishedMutex);
		finished = m_finished;	
		pthread_mutex_unlock(&m_finishedMutex);
		return finished;
	}
	// Marks that all producers have reached their quotas.
	void MarkAsFinishedProducing() {
		pthread_mutex_lock(&m_finishedMutex);
		m_finished = true;
		pthread_mutex_unlock(&m_finishedMutex);
	}
private:
	pthread_mutex_t m_outputMutex;
	pthread_mutex_t m_finishedMutex;
	pthread_mutex_t m_flightMutex;
	bool m_finished;
	unsigned long m_inFlight;
};

/* *
 * Class CircularBuffer
 *
 * Thread-safe container class implementing a circular buffer.
 * */
class CircularBuffer {
public:
	CircularBuffer(unsigned long buffSize, unsigned long id, ConComm& comm) 
	:	m_size(buffSize), m_pBuffer(new int[m_size]), m_id(id), m_comm(comm)
	{
		m_front = 0;
		m_rear = 0;
		m_itemCount = 0;
		pthread_mutex_init(&m_mutex, NULL);
	}
	~CircularBuffer() {
		delete[] m_pBuffer;
		pthread_mutex_destroy(&m_mutex);
	}
	// Inserts item into buffer if it is not full. Returns whether this was successful.
	bool PutIfNotFull(int item, Task const& t) {
		bool success = false;
		pthread_mutex_lock(&m_mutex);

		// Checks if there is space in the buffer
		if (m_itemCount != m_size) {
			m_pBuffer[m_front] = item;
			m_front = (m_front + 1) % m_size;
			++m_itemCount;
			success = true;
		}
		else {
			success = false;
		}

		pthread_mutex_unlock(&m_mutex);
		if (success)
			m_comm.PrintOut(t);
		return success;
	}
	// Sets item to be next item in buffer if not empty. Returns success.
	bool GetIfNotEmpty(int& item, Task const& t) {
		item = -1;
		bool success = false;
		pthread_mutex_lock(&m_mutex);

		// Checks if there are items in buffer
		if (m_itemCount != 0) {
			item = m_pBuffer[m_rear];
			m_rear = (m_rear + 1) % m_size;
			--m_itemCount;
			success = true;
		}
		else {
			success = false;	
		}

		pthread_mutex_unlock(&m_mutex);
		if (success)
			m_comm.PrintOut(t);
		return success;
	}
	// Prints status. DANGER - assumes mutex already locked.
	void PrintStatus() {
		std::cout << "B" << m_id << ":";
		for (unsigned long i = m_rear; i != m_front; i=(i+1)%m_size)
			std::cout << m_pBuffer[i] << " ";
		std::cout << std::endl << m_rear << " " << m_front;
	}
private:
	unsigned long m_front;			// Front of the buffer. Items inserted here.
	unsigned long m_rear;			// Rear of the buffer. Items removed from here.
	unsigned long const m_size;		// Max size of the buffer.
	unsigned long m_itemCount;		// Current number of items in buffer.
	int* const m_pBuffer;			// The buffer itself.
	unsigned long const m_id;		// ID of this buffer.
	ConComm& m_comm;					// Concurrent communicator.
	pthread_mutex_t m_mutex;		// Mutex providing read/write locking.
};

/* *
 * Class Producer
 *
 * Implements the Task interface. Ventilator, creates items and puts them in buffers.
 * */
class Producer : public Task {
public:
	Producer(std::vector<CircularBuffer*> const& buffers, ConComm& comm, 
				unsigned long id, unsigned long itemCount)
	:	m_buffers(buffers), m_comm(comm), m_id(id), m_itemCount(itemCount)
	{
		m_produced = 0;
		m_lastBufferUsed = NULL;
	}
	void Execute() {
		int item = rand() % 100;
		while (m_produced < m_itemCount) {
			bool success = false;
			for (unsigned long i = 0; i < m_buffers.size(); ++i) {
				m_lastBufferUsed = m_buffers[i];
				success = m_buffers[i]->PutIfNotFull(item, *this);
				if (success)
					break;
			}
			if (success) {
				item = rand() % 100;
				++m_produced;
				//m_comm.IncrementInFlight();
			}
			else {
				//wait for signal
			}
		}
		m_comm.PrintExit(*this);
	}
	void PrintStatus() const {
		std::cout << "Producer " << m_id << ":  ";
		if (m_lastBufferUsed != NULL)
			m_lastBufferUsed->PrintStatus();
		else
			std::cout << "ERROR: last buffer not assigned";
		std::cout << std::endl;
	}
	void PrintExit() const {
		std::cout << "Producer " << m_id << " is exiting" << std::endl;
	}
private:
	std::vector<CircularBuffer*> const& m_buffers;
	ConComm& m_comm;
	unsigned long const m_id;
	unsigned long const m_itemCount;
	unsigned long m_produced;
	CircularBuffer* m_lastBufferUsed;
};

/* *
 * Class Consumer
 *
 * Implements the Task interface. Worker, consumes items from buffers.
 * */
class Consumer : public Task {
public:
	Consumer(std::vector<CircularBuffer*> const& buffers, ConComm& comm,
				unsigned long id) 
	:	m_buffers(buffers), m_comm(comm), m_id(id)	
	{
		m_lastBufferUsed = NULL;	
	}
	void Execute() {
		while (true) {
			bool success = false;
			for (unsigned long i = 0; i < m_buffers.size(); ++i) {
				int item = 0;
				m_lastBufferUsed = m_buffers[i];
				success = m_buffers[i]->GetIfNotEmpty(item, *this);
				if (success)
					break;
			}
			if (success) {
				//m_comm.DecrementInFlight();
				sleep(1);
			}
			else {
				// wait for signal
			}
		}
		m_comm.PrintExit(*this);
	}
	void PrintStatus() const {
		std::cout << "Consumer " << m_id << ":  ";
		if (m_lastBufferUsed != NULL)
			m_lastBufferUsed->PrintStatus();
		else
			std::cout << "ERROR: last buffer not assigned";
		std::cout << std::endl;
	}
	void PrintExit() const {
		std::cout << "Consumer " << m_id << " is exiting" << std::endl;
	}
private:
	std::vector<CircularBuffer*> const& m_buffers;
	ConComm& m_comm;
	unsigned long m_id;
	CircularBuffer* m_lastBufferUsed;
};

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
	comm.MarkAsFinishedProducing();

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

	return 0;
}

