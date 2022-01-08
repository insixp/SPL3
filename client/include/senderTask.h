#ifndef SENDER_TASK_H_
#define SENDER_TASK_H_

#include <connectionHandler.h>

class senderTask {
    private:
        ConnectionHandler&  connectionHandler;
        int                 _id = -1;
        bool                terminate = false;
    public:
        senderTask(ConnectionHandler& connectionHandler, int _id);
        void shouldTerminate();
        void run();
};

#endif
