#ifndef SENDER_TASK_H_
#define SENDER_TASK_H_

#include <connectionHandler.h>

class senderTask {
    private:
        ConnectionHandler&  connectionHandler;
        std::mutex&         mutex;
        int                 _id;
    public:
        senderTask(ConnectionHandler& connectionHandler, std::mutex& mutex, int _id);
        void run();
};

#endif
