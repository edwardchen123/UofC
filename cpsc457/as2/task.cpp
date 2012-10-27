#include "task.hpp"
#include <iostream>

Producer::Producer(std::vector<CircularBuffer*> const& buffers, 
                    ConComm& comm, 
                    unsigned long id, 
                    unsigned long itemCount)
                    : m_buffers(buffers), m_comm(comm), 
                        m_id(id), m_itemCount(itemCount)
{
    m_produced = 0;
}

void Producer::Execute() {
    m_buffers[0]->PutIfNotFull(10, *this);
}

void Producer::PrintStatus() const {
    std::cout << "Producer " << m_id << ":  ";
}

Consumer::Consumer(std::vector<CircularBuffer*> const& buffers, 
                    ConComm& comm,
                    unsigned long id)
                    : m_buffers(buffers), m_comm(comm), m_id(id)
{
    // No additional setup necessary
}

void Consumer::Execute() {
    while (!m_comm.IsFinished()) { }
    int x = 0;
    m_buffers[0]->GetIfNotEmpty(x, *this);
}

void Consumer::PrintStatus() const {
    std::cout << "Consumer " << m_id << ":  ";
}

