g++ -o circular_buffer.o -c -pthread circular_buffer.cpp
g++ -o con_comm.o -c -pthread con_comm.cpp
g++ -o pc.o -c -pthread pc.cpp
g++ -o task.o -c -pthread task.cpp
g++ -o a.out -pthread circular_buffer.o con_comm.o pc.o task.o
