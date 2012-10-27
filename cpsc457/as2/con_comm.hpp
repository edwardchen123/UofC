#pragma once

#include <pthread.h>
#include "task.hpp"
#include "circular_buffer.hpp"

class CircularBuffer;
class Task;

/* *
 * Class ConComm
 *
 * Concurrent communicator. Used to send signals and synchronize between threads.
 * */
class ConComm {
public:
	ConComm(); 
	~ConComm();
	void PrintOut(Task const& outT, CircularBuffer const& outB);
    void DebugPrintOut(char const* out);
	bool IsFinished();
	void MarkAsFinished();
private:
	pthread_mutex_t m_outputMutex;
	pthread_mutex_t m_finishedMutex;
	bool m_finished;
};

