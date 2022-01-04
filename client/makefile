CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread -lboost_thread

all: BGSClient
	g++ -o bin/BGSClient bin/connectionHandler.o bin/BGSClient.o bin/utils.o bin/senderTask.o $(LDFLAGS) 

BGSClient: bin/connectionHandler.o bin/BGSClient.o bin/utils.o bin/senderTask.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BGSClient.o: src/BGSClient.cpp
	g++ $(CFLAGS) -o bin/BGSClient.o src/BGSClient.cpp

bin/utils.o: src/utils.cpp
	g++ $(CFLAGS) -o bin/utils.o src/utils.cpp

bin/senderTask.o: src/senderTask.cpp
	g++ $(CFLAGS) -o bin/senderTask.o src/senderTask.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
