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

    senderTask senderTask_(connectionHandler, 1);
    std::thread senderThread_(&senderTask::run, &senderTask_);
    bool terminate = false;

    while (!terminate) {
        std::string message;
        OP_CODES    opcode;
        try{
            char short_bytes[2];
            connectionHandler.getShort(short_bytes);
            opcode = (OP_CODES)bytesToShort(short_bytes);
            if(opcode == ACK){
                connectionHandler.getShort(short_bytes);
                OP_CODES preMsgOpCode = (OP_CODES)bytesToShort(short_bytes);
                std::string ACK_print = "ACK " + std::to_string(preMsgOpCode) + " ";
                if(preMsgOpCode == FOLLOW) {
                    std::string username;
                    std::string dump;
                    connectionHandler.getString(username);
                    ACK_print += username;
                }
                else if(preMsgOpCode == LOGGED_IN_STATS || preMsgOpCode == STATS){
                    connectionHandler.getRestOfMessage(message, ';');
                    ACK_print += message.substr(0, message.size()-2);
                }
                else if(preMsgOpCode == LOGOUT) {
                    terminate = true;
                    senderTask_.shouldTerminate();
                }
                std::cout << ACK_print << std::endl;
                connectionHandler.getRestOfMessage(message, ';');
            }
            else if(opcode == ERROR){
                connectionHandler.getShort(short_bytes);
                OP_CODES preMsgOpCode = (OP_CODES)bytesToShort(short_bytes);
                std::cout << "Message(" + std::to_string(preMsgOpCode) + ") error" << std::endl;
                connectionHandler.getRestOfMessage(message, ';');
            }
            else if(opcode == NOTIFICATION){
                char notificationType[1];
                std::string notiType;
                std::string username;
                std::string content;
                connectionHandler.getBytes(notificationType, 1);
                if(notificationType[0] == '\0')
                    notiType = "PM";
                else
                    notiType = "Public";
                connectionHandler.getString(username);
                connectionHandler.getRestOfMessage(content, ';');
                std::cout << "NOTIFICATION " << notiType << " " << username << " " << content.substr(0, content.size()-2) << std::endl;
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

