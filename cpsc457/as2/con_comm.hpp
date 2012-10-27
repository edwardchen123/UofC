#pragma once

#include <pthread.h>
#include <sstream>
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
    // Prints out state of given task and buffer
	void PrintOut(Task const& outT, CircularBuffer const& outB);
    // Prints out given stringstream
    void PrintOut(std::stringstream const& ss);
    // Returns whether producers have finished production
	bool IsFinished();
    // Mark that producers have finished production
	void MarkAsFinished();
private:
	pthread_mutex_t m_outputMutex;
	pthread_mutex_t m_finishedMutex;
	bool m_finished;
};

