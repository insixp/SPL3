#ifndef CONNECTION_HANDLER__
#define CONNECTION_HANDLER__
                                           
#include <string>
#include <iostream>
#include <messages.h>
#include <boost/asio.hpp>

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
	const std::string host_;
	const short port_;
	boost::asio::io_service io_service_;   // Provides core I/O functionality
	tcp::socket socket_; 
 
public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();
 
    void connect();
 
    void getBytes(char bytes[], unsigned int bytesToRead);
    void sendBytes(const char bytes[], int bytesToWrite);
    void getString(std::string& frame);
    void getShort(char bytes[]);
    void getRestOfMessage(std::string& frame, char delimiter);
    void SendMessageASCII(const OP_CODES& opcode, const std::string& frame, char delimiter);
	
    void close();
 
}; //class ConnectionHandler
 
#endif