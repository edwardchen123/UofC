#include <iostream>
#include <cstdio>
#include <pthread.h>

struct SumRangeArgs {
    unsigned long low;
    unsigned long high;
    unsigned long sum;
    pthread_t id;
};

void* SumRange(void* varg) {
    SumRangeArgs* arg = static_cast<SumRangeArgs*>(varg);
    for (unsigned long i = arg->low; i <= arg->high; ++i) {
        arg->sum += i; 
    }
}

int main(int argc, char* argv[]) {	
    if (argc != 3) {
        std::cout << "Incorrect program usage." << std::endl;
        return 0;
    }
    unsigned long N = strtoul(argv[1], NULL, 10);
    unsigned long T = strtoul(argv[2], NULL, 10);
    unsigned long blockSize = N/T;

    SumRangeArgs* args[T];
    unsigned  long i = 0;
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

    for (unsigned long i = 0; i < T; ++i) {
        pthread_join(args[i]->id, NULL);
    }

    unsigned long sum = 0;
    for (unsigned long i = 0; i < T; ++i) {
        sum += args[i]->sum;
    }
    printf("Sum(1, %lu) = %lu\n", N, sum);
    //printf("Sum(1, %lu) = %lu\n", N, (N*(N+1)/2));

	return 0;
}

