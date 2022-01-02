package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.ErrorMsg;
import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.BGSServer.Messages.RegisterMsg;

public class BGSProtocol implements BidiMessagingProtocol<Message> {

    private Connections<Message>    connections;
    private Database                db;
    private int                     connectionId;

    public BGSProtocol(){
        this.connections = null;
        this.connectionId = -1;
        this.db = new Database();
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message) {
        message.process(this.db, this.connections, this.connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
