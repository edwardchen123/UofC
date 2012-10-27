#include "task.hpp"
#include <iostream>
#include <math.h>
#include <time.h>

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
    int item = rand() % 100;
    while (m_produced < m_itemCount) {
        bool success = false;
        for (unsigned long i = 0; i < m_buffers.size(); ++i) {
            success = m_buffers[i]->PutIfNotFull(item, *this);
            if (success)
                break;
        }
        if (success) {
            ++m_produced;
            item = rand() % 100;    
        }
        else {
            // wait for signal
        }
    }
    std::stringstream ss;
    ss << "Producer " << m_id << " is exiting." << std::endl;
    m_comm.PrintOut(ss);
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
    while (true) {
        bool success = false;
        bool finished = m_comm.IsFinished();
        for (unsigned long i = 0; i < m_buffers.size(); ++i) {
            int item = 0;
            success = m_buffers[i]->GetIfNotEmpty(item, *this);
            if (success)
                break;
        }
        if (success) {
            sleep(1);
        }
        else {
            if (finished)
                break;
            // wait for signal
        }
    }
    std::stringstream ss;
    ss << "Consumer " << m_id << " is exiting." << std::endl;
    m_comm.PrintOut(ss);
}

void Consumer::PrintStatus() const {
    std::cout << "Consumer " << m_id << ":  ";
}

