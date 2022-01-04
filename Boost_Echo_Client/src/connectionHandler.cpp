#include <connectionHandler.h>
#include <utils.h>
 
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
void ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        throw;
    }
}
 
void ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        throw;
    }
}

void ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        throw;
    }
}

void ConnectionHandler::getString(std::string& frame) {
    char ch;
    try {
		do{
			getBytes(&ch, 1);
            frame.append(1, ch);
        }while (ch != '\0');
    } catch (std::exception& e) {
        throw;
    }
}

void ConnectionHandler::getShort(char bytes[]){
    try {
        getBytes(&bytes[0], 1);
        getBytes(&bytes[1], 1);
    } catch (std::exception& e) {
        throw;
    }
}

void ConnectionHandler::getRestOfMessage(std::string& frame, char delimiter){
    char ch;
    try {
		do{
			getBytes(&ch, 1);
            frame.append(1, ch);
        }while (ch != delimiter);
    } catch (std::exception& e) {
        throw;
    }
}
 
void ConnectionHandler::SendMessageASCII(const OP_CODES& opcode, const std::string& frame, char delimiter) {
    char opcode_bytes[frame.length() + 3];
    shortToBytes(opcode, opcode_bytes);
    std::copy(frame.c_str(), frame.c_str() + frame.length(), &opcode_bytes[2]);
    opcode_bytes[frame.length() + 2] = ';';
    try{
	    sendBytes(opcode_bytes, frame.length() + 3);
    } catch(std::exception& e) {
        throw;
    }
	
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}