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
    try {
        connectionHandler.connect();
    } catch (std::exception& e) {
        std::cerr << "Cannot connect to " << host << ":" << port << " - " << e.what() << std::endl;
        return 1;
    }

    std::mutex mutex;
    senderTask senderTask_(connectionHandler, mutex, 1);
    std::thread senderThread_(&senderTask::run, &senderTask_);

    while (1) {
        std::string message;
        OP_CODES    opcode;
        try{
            char short_bytes[2];
            connectionHandler.getShort(short_bytes);
            opcode = (OP_CODES)bytesToShort(short_bytes);
            if(opcode == ACK){
                connectionHandler.getShort(short_bytes);
                OP_CODES preMsgOpCode = (OP_CODES)bytesToShort(short_bytes);
                std::cout << "Message(" + std::to_string(preMsgOpCode) + ") recieved" << std::endl;
                connectionHandler.getRestOfMessage(message, ';');
            }
            else if(opcode == ERROR){
                connectionHandler.getShort(short_bytes);
                OP_CODES preMsgOpCode = (OP_CODES)bytesToShort(short_bytes);
                std::cout << "Message(" + std::to_string(preMsgOpCode) + ") error" << std::endl;
                connectionHandler.getRestOfMessage(message, ';');
            }
            else if(opcode == NOTIFICATION){
                connectionHandler.getShort(short_bytes);
                OP_CODES preMsgOpCode = (OP_CODES)bytesToShort(short_bytes);
                std::cout << "Message(" + std::to_string(preMsgOpCode) + ") notification" << std::endl;
                connectionHandler.getRestOfMessage(message, ';');
            } else {
                std::cerr << "Recieved unrecognized message type" << std::endl;
                continue;
            }
        } catch (std::exception& e) {
            std::cout << "Could not recieve message (" << e.what() << ")" << std::endl;
            break;
        }
    }
    senderThread_.join();
    return 0;
}

