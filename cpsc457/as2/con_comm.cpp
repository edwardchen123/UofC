#include "con_comm.hpp"
#include <iostream>

ConComm::ConComm() { 
    pthread_mutex_init(&m_outputMutex, NULL);
    pthread_mutex_init(&m_finishedMutex, NULL);
    m_finished = false;	
}

ConComm::~ConComm() {
    pthread_mutex_destroy(&m_outputMutex);
    pthread_mutex_destroy(&m_finishedMutex);
}

void ConComm::PrintOut(Task const& outT, CircularBuffer const& outB) {
    pthread_mutex_lock(&m_outputMutex);
    outT.PrintStatus();
    outB.PrintStatus();
    std::cout << std::endl;
    pthread_mutex_unlock(&m_outputMutex);
}

void ConComm::PrintOut(std::stringstream const& ss) {
    pthread_mutex_lock(&m_outputMutex);
    std::cout << ss.str();
    pthread_mutex_unlock(&m_outputMutex);
}

bool ConComm::IsFinished() {
    bool finished = false;
    pthread_mutex_lock(&m_finishedMutex);
    finished = m_finished;	
    pthread_mutex_unlock(&m_finishedMutex);
    return finished;
}

void ConComm::MarkAsFinished() {
    pthread_mutex_lock(&m_finishedMutex);
    m_finished = true;
    pthread_mutex_unlock(&m_finishedMutex);
}

