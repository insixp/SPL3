#include <senderTask.h>
#include <messages.h>
#include <utils.h>
#include <connectionHandler.h>
#include <string>
#include <vector>
#include <iostream>
#include <ctime>

senderTask::senderTask(ConnectionHandler& connectionHandler, int _id): 
connectionHandler(connectionHandler){
    this-> terminate = false;
    this->_id = _id;
}

void senderTask::run(){
    std::vector<std::string> command(0);

    std::string line;

    while(!this->terminate){

        command.clear();
        getline(std::cin, line);

        std::istringstream stream(line);
        // Parse command
        std::string word;
        std::string message;
        OP_CODES opcode;
        while (stream >> word) {
            command.push_back(word);
        }

        if(command[0].compare("REGISTER") == 0){
            if(command.size() != 4){
                std::cout << "Register command must have 3 arguments" << std::endl;
                continue;
            }
            message = command[1] + '\0' + command[2] + '\0' + command[3] + '\0';
            opcode = OP_CODES::REGISTER;
        }
        else if(command[0].compare("LOGIN") == 0){
            if(command.size() != 4){
                std::cout << "Login command must have 3 arguments" << std::endl;
                continue;
            }
            char capcha;
            if(command[3].compare("1") == 0)
                capcha = '\1';
            else
                capcha = '\0';
            message = command[1] + '\0' + command[2] + '\0' + capcha;
            opcode = OP_CODES::LOGIN;
        }
        else if(command[0].compare("LOGOUT") == 0){
            if(command.size() != 1){
                std::cout << "Logout command must have 0 arguments" << std::endl;
                continue;
            }
            message = "";
            opcode = OP_CODES::LOGOUT;
        }
        else if(command[0].compare("FOLLOW") == 0){
            if(command.size() != 3 || command[1].size() != 1){
                std::cout << "Follow command must have 2 arguments and argument 1 must be 0/1" << std::endl;
                continue;
            }
            char follow_unfollow;
            if(command[1].compare("0") == 0)
                follow_unfollow = '\0';
            else
                follow_unfollow = '\1';
            message = follow_unfollow + command[2] + '\x00';
            opcode = OP_CODES::FOLLOW;
        }
        else if(command[0].compare("POST") == 0){
            if(command.size() < 1){
                std::cout << "POST command must have atleast 1 argument" << std::endl;
                continue;
            }
            message += command[1];
            for(size_t i = 2; i < command.size(); i++)
                message +=  " " + command[i];
            message += '\x00';
            opcode = OP_CODES::POST;
        }
        else if(command[0].compare("PM") == 0){
            if(command.size() < 2){
                std::cout << "PM command must have atleast 2 arguments" << std::endl;
                continue;
            }
            std::time_t now = std::time(nullptr);
            std::tm* info = std::localtime(&now);
            char buff[64];
            std::strftime(buff, 64, "%d-%m-%Y %H:%M", info);
            std::string time = std::string(buff);
            message = command[1] + '\x00';
            message += command[2];
            for(size_t i = 3; i < command.size(); i++)
                message +=  " " + command[i];
            message += '\x00' + time + '\x00';
            opcode = OP_CODES::PM;
        }
        else if(command[0].compare("LOGSTAT") == 0){
            if(command.size() != 1){
                std::cout << "LOGSTAT command must have 0 arguments" << std::endl;
                continue;
            }
            message = "";
            opcode = OP_CODES::LOGGED_IN_STATS;
        }
        else if(command[0].compare("STAT") == 0){
            if(command.size() < 2){
                std::cout << "STAT command must have 1 arguments" << std::endl;
                continue;
            }
            std::string usernames;
            usernames += command[1];
            for(size_t i = 2; i < command.size(); i++)
                usernames +=  "|" + command[i];
            message = usernames + '\x00';
            opcode = OP_CODES::STATS;
        }
        else if(command[0].compare("BLOCK") == 0){
            if(command.size() != 2){
                std::cout << "STAT command must have 1 arguments" << std::endl;
                continue;
            }
            message = command[1] + '\x00';
            opcode = OP_CODES::BLOCK;
        } else {
            std::cout << "Command not found" << std::endl;
            continue;
        }

        try{
            this->connectionHandler.SendMessageASCII(opcode, message, ';');
        } catch (std::exception& e){
            std::cout << "Could not send message" << std::endl;
            break;
        }
    }
}

void senderTask::shouldTerminate(){
    this->terminate = true;
}