#include <senderTask.h>
#include <messages.h>
#include <utils.h>
#include <connectionHandler.h>
#include <string>
#include <vector>
#include <iostream>

senderTask::senderTask(ConnectionHandler& connectionHandler, std::mutex& mutex, int _id): 
connectionHandler(connectionHandler),
mutex(mutex),
_id(_id){}

void senderTask::run(){
    std::vector<std::string> command(0);

    std::string line;
    
    while(1){
        getline(std::cin, line);
    
        std::istringstream stream(line);
        // // Parse command
        std::string word;
        while (stream >> word) {
            command.push_back(word);
        }
         if(command[0].compare("REGISTER") == 0){
            this->mutex.lock();
            this->connectionHandler.SendMessageASCII(OP_CODES::REGISTER, command[1] + '\0' + command[2] + '\0' + command[3] + '\0', ';');
            this->mutex.unlock();
        }
    }
}