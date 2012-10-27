#pragma once

#include <pthread.h>
#include "task.hpp"
#include "con_comm.hpp"

class Task;
class ConComm;

/* *
 * Class CircularBuffer
 *
 * Thread-safe container class implementing a circular buffer.
 * */
class CircularBuffer {
public:
	CircularBuffer(unsigned long buffSize, unsigned long id, ConComm& comm);
	~CircularBuffer();
	bool PutIfNotFull(int item, Task const& t);
	bool GetIfNotEmpty(int& item, Task const& t);
    void PrintStatus() const;
private:
	unsigned long m_front;
	unsigned long m_rear;
	unsigned long const m_size;
	unsigned long m_itemCount;
	int* const m_pBuffer;
    unsigned long const m_id;
    ConComm& m_comm;
	pthread_mutex_t m_mutex;
};

