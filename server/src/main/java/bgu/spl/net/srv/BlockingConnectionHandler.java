package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.ConnectionHandler;
import bgu.spl.net.api.bidi.Connections;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private Semaphore semaphore;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.semaphore = new Semaphore(1);
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    public boolean trySend(T msg){
        if(this.semaphore.tryAcquire()){
            this.sendMsg(msg);
            this.semaphore.release();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void send(T msg) {
        try{
            this.semaphore.acquire();
            this.sendMsg(msg);
            this.semaphore.release();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMsg(T msg) {
        try{
            out = new BufferedOutputStream(this.sock.getOutputStream());
            out.write(this.encdec.encode(msg));
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void start(int connectionId, Connections<T> connections){
        this.protocol.start(connectionId, connections);
    }
}
