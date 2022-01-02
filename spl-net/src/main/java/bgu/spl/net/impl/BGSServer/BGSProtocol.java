package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.ErrorMsg;
import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.BGSServer.Messages.RegisterMsg;

import java.io.IOException;

public class BGSProtocol implements BidiMessagingProtocol<Message> {

    private Connections<Message>    connections;
    private Database                db;
    private int                     connectionId;

    public BGSProtocol(){
        this.connections = null;
        this.connectionId = -1;
        this.db = Database.getInstance();
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message) {
        try {
            message.process(this.db, this.connections, this.connectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
