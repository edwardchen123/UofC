#include "circular_buffer.hpp"
#include <iostream>

CircularBuffer::CircularBuffer(unsigned long buffSize, unsigned long id, ConComm& comm) 
:	m_size(buffSize), m_pBuffer(new int[m_size]), m_id(id), m_comm(comm)
{
    m_front = 0;
    m_rear = 0;
    m_itemCount = 0;
    pthread_mutex_init(&m_mutex, NULL);
}

CircularBuffer::~CircularBuffer() {
    delete[] m_pBuffer;
    pthread_mutex_destroy(&m_mutex);
}

bool CircularBuffer::PutIfNotFull(int item, Task const& t) {
    bool success = false;
    pthread_mutex_lock(&m_mutex);

    // Checks if there is space in the buffer
    if (m_itemCount != m_size) {
        m_pBuffer[m_front] = item;
        m_front = (m_front + 1) % m_size;
        ++m_itemCount;
        success = true;
        m_comm.PrintOut(t, *this);
    }
    else {
        success = false;
    }

    pthread_mutex_unlock(&m_mutex);
    return success;
}

bool CircularBuffer::GetIfNotEmpty(int& item, Task const& t) {
    item = -1;
    bool success = false;
    pthread_mutex_lock(&m_mutex);

    // Checks if there are items in buffer
    if (m_itemCount != 0) {
        item = m_pBuffer[m_rear];
        m_rear = (m_rear + 1) % m_size;
        --m_itemCount;
        success = true;
        m_comm.PrintOut(t, *this);
    }
    else {
        success = false;	
    }

    pthread_mutex_unlock(&m_mutex);
    return success;
}

void CircularBuffer::PrintStatus() const {
    std::cout << "B" << m_id << ":";
    for (unsigned long i = 0; i < m_itemCount; ++i)
        std::cout << m_pBuffer[(m_rear+i)%m_size] << " ";
}

