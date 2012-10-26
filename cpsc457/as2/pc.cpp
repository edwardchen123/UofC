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

class Task {
public:
	virtual void Execute() = 0;
	virtual void PrintStatus() const = 0;
};


class ConComm {
public:
	ConComm() { 
		pthread_mutex_init(&m_outputMutex, NULL);
		pthread_mutex_init(&m_finishedMutex, NULL);
		m_finished = false;	
	}
	~ConComm() {
		pthread_mutex_destroy(&m_outputMutex);
		pthread_mutex_destroy(&m_finishedMutex);
	}
	void PrintOut(Task const& out) {
		pthread_mutex_lock(&m_outputMutex);
		out.PrintStatus();
		pthread_mutex_unlock(&m_outputMutex);
	}
	bool IsFinished() {
		bool finished = false;
		pthread_mutex_lock(&m_finishedMutex);
		finished = m_finished;	
		pthread_mutex_unlock(&m_outputMutex);
		return finished;
	}
	void MarkAsFinished() {
		pthread_mutex_lock(&m_finishedMutex);
		m_finished = true;
		pthread_mutex_unlock(&m_outputMutex);
	}
private:
	pthread_mutex_t m_outputMutex;
	pthread_mutex_t m_finishedMutex;
	bool m_finished;
};

class CircularBuffer {
public:
	CircularBuffer(unsigned long buffSize) 
	:	m_size(buffSize), m_pBuffer(new int[m_size])
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
	bool PutIfNotFull(int item) {
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
		return success;
	}
	int GetIfNotEmpty(bool& success) {
		int item = -1;
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
		return item;
	}
private:
	unsigned long m_front;
	unsigned long m_rear;
	unsigned long const m_size;
	unsigned long m_itemCount;
	int* const m_pBuffer;
	pthread_mutex_t m_mutex;
};

class Producer : public Task {
public:
	Producer(std::vector<CircularBuffer*> const& buffers, ConComm& comm, 
				unsigned long id, unsigned long itemCount)
	:	m_buffers(buffers), m_comm(comm), m_id(id), m_itemCount(itemCount)
	{
		m_produced = 0;
	}
	void Execute() {

	}
	void PrintStatus() const {

	}
private:
	std::vector<CircularBuffer*> const& m_buffers;
	ConComm& m_comm;
	unsigned long const m_id;
	unsigned long const m_itemCount;
	unsigned long m_produced;
};

class Consumer : public Task {
public:
	Consumer(std::vector<CircularBuffer*> const& buffers, ConComm& comm,
				unsigned long id) 
	:	m_buffers(buffers), m_comm(comm), m_id(id)	
	{ }
	void Execute() {

	}
	void PrintStatus() const {

	}
private:
	std::vector<CircularBuffer*> const& m_buffers;
	ConComm& m_comm;
	unsigned long m_id;
};

// Dispatch function called on thread launch
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
		buffers.push_back(new CircularBuffer(buffSize));

	// Creates and launches producers
	std::vector< std::pair<Producer*, unsigned long> > producers;
	for (unsigned long i = 0; i < prodCount; ++i) {
		Producer* p = new Producer(buffers, comm, i, itemCount);
		unsigned long threadId = 0;
		int error = pthread_create(&threadId, NULL, Dispatch, static_cast<void*>(p));
		producers.push_back(std::pair<Producer*, unsigned long>(p, threadId));
	}

	// Creates and launches consumers
	std::vector< std::pair<Consumer*, unsigned long> > consumers;
	for (unsigned long i = 0; i < consCount; ++i) {
		Consumer* c = new Consumer(buffers, comm, i);
		unsigned long threadId = 0;
		int error = pthread_create(&threadId, NULL, Dispatch, static_cast<void*>(c));
		consumers.push_back(std::pair<Consumer*, unsigned long>(c, threadId));
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

	return 0;
}

