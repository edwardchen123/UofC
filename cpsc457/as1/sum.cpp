/**********************************************
Last Name: Helwer
First Name: Andrew
Student ID: 10023875
Course: CPSC 457
Tutorial Section: T05
Assignment: 1
Question: 1
File name: sum.cpp
**********************************************/

#include <iostream>
#include <cstdio>
#include <pthread.h>

// Collection of fields to be associated with each thread
struct SumRangeArgs {
    unsigned long low;
    unsigned long high;
    unsigned long sum;
    pthread_t id;
};

// Function called by each thread. Sums given range.
void* SumRange(void* varg) {
    SumRangeArgs* arg = static_cast<SumRangeArgs*>(varg);
    for (unsigned long i = arg->low; i <= arg->high; ++i) {
        arg->sum += i; 
    }
}

int main(int argc, char* argv[]) {	
    // Checks for correct program usage
    if (argc != 3) {
        std::cout << "Incorrect program usage." << std::endl;
        return 0;
    }
    // Parses command line arguments
    unsigned long N = strtoul(argv[1], NULL, 10); // Input size
    unsigned long T = strtoul(argv[2], NULL, 10); // Thread count
    unsigned long blockSize = N/T;

    // Spawns threads
    SumRangeArgs* args[T];
    unsigned long i = 0;
    for (i = 0; i < T-1; ++i) {
        args[i] = new SumRangeArgs();
        args[i]->low = i*blockSize+1;
        args[i]->high = (i+1)*blockSize;
        args[i]->sum = 0;
        int error = pthread_create(&(args[i]->id), NULL, SumRange, (void*)args[i]);
        printf("Thread %lu: (%lu, %lu)\n", args[i]->id, args[i]->low, args[i]->high);
    }
    args[i] = new SumRangeArgs();
    args[i]->low = i*blockSize+1;
    args[i]->high = N;
    args[i]->sum = 0;
    int error = pthread_create(&(args[i]->id), NULL, SumRange, (void*)args[i]);
    printf("Thread %lu: (%lu, %lu)\n", args[i]->id, args[i]->low, args[i]->high);

    // Waits for all threads to terminate
    for (unsigned long i = 0; i < T; ++i) {
        pthread_join(args[i]->id, NULL);
    }

    // Sums up values returned by threads
    unsigned long sum = 0;
    for (unsigned long i = 0; i < T; ++i) {
        sum += args[i]->sum;
    }

    // Prints out sum
    printf("Sum(1, %lu) = %lu\n", N, sum);

    // Frees allocated memory
    for (unsigned long i = 0; i < T; ++i) {
        delete args[i];
    }

	return 0;
}

