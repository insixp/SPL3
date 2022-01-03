#include <stdlib.h>
#include <connectionHandler.h>
#include <senderTask.h>
#include <messages.h>
#include <utils.h>
#include <thread>

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::mutex mutex;
    senderTask senderTask_(connectionHandler, mutex, 1);
    std::thread senderThread_(&senderTask::run, &senderTask_);
	
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
    }
    return 0;
}

