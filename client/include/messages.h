#ifndef MESSAGES_H_
#define MESSAGES_H_

enum OP_CODES {
    REGISTER = (short)1,
    LOGIN = (short)2,
    LOGOUT = (short)3,
    FOLLOW = (short)4,
    POST = (short)5,
    PM = (short)6,
    LOGGED_IN_STATS = (short)7,
    STATS = (short)8,
    NOTIFICATION = (short)9,
    ACK = (short)10,
    ERROR = (short)11,
    BLOCK = (short)12
};

#endif
