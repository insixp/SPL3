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
                    connectionHandler.getRestOfMessage(message, ';');
                }
                else if(preMsgOpCode == LOGGED_IN_STATS || preMsgOpCode == STATS){
                    std::string d;
                    int index = 0;
                    size_t DATASIZE = 12;
                    short age;
                    short posts;
                    short followers;
                    short following;
                    connectionHandler.getRestOfMessage(d, ';');
                    char data[d.length()+4];
                    data[0] = '\x00';
                    data[1] = '\x10';
                    data[2] = '\x00';
                    data[3] = '\x07';
                    d.copy(&data[4], d.length());
                    while(data[index*DATASIZE] != ';'){
                        size_t pos = index*DATASIZE;
                        opcode = (OP_CODES)bytesToShort(&data[pos]);
                        preMsgOpCode = (OP_CODES)bytesToShort(&data[pos+2]);
                        age = bytesToShort(&data[pos+4]);
                        posts = bytesToShort(&data[pos+6]);
                        followers = bytesToShort(&data[pos+8]);
                        following = bytesToShort(&data[pos+10]);
                        if(index != 0){
                            ACK_print += "\n";
                            ACK_print +=  std::to_string(opcode) + " " + std::to_string(preMsgOpCode) + " ";
                        }
                        ACK_print +=  std::to_string(age) + " " + std::to_string(posts) + " " +
                                    std::to_string(followers) + " " + std::to_string(following);
                        index++;
                    }
                }
                else if(preMsgOpCode == LOGOUT) {
                    terminate = true;
                    pthread_cancel(senderThread_.native_handle());
                    connectionHandler.getRestOfMessage(message, ';');
                } else {
                    connectionHandler.getRestOfMessage(message, ';');
                }
                std::cout << ACK_print << std::endl;
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

