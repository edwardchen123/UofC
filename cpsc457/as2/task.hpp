#pragma once

#include <vector>
#include "circular_buffer.hpp"
#include "con_comm.hpp"

class CircularBuffer;
class ConComm;

/* *
 * Class Task
 *
 * Interface implemented by tasks to be executed by threads.
 * */
class Task {
public:
    // Executes task
	virtual void Execute() = 0;
    // Prints status of task
	virtual void PrintStatus() const = 0;
};

/* *
 * Class Producer
 *
 * Ventilator class implementing Task interface. Produces items.
 * */
class Producer : public Task {
public:
	Producer(std::vector<CircularBuffer*> const& buffers, 
                ConComm& comm, 
				unsigned long id, 
                unsigned long itemCount);
	void Execute();
	void PrintStatus() const;
private:
    std::vector<CircularBuffer*> const& m_buffers;
	ConComm& m_comm;
	unsigned long const m_id;
	unsigned long const m_itemCount;
	unsigned long m_produced;
};

/* *
 * Class Consumer
 *
 * Worker class implementing Task interface. Consumes items.
 * */
class Consumer : public Task {
public:
	Consumer(std::vector<CircularBuffer*> const& buffers, 
                ConComm& comm,
				unsigned long id);
	void Execute();
	void PrintStatus() const;
private:
	std::vector<CircularBuffer*> const& m_buffers;
	ConComm& m_comm;
	unsigned long m_id;
};

