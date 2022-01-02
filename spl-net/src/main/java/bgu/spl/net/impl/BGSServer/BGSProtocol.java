package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.BGSServer.Messages.RegisterMsg;
import bgu.spl.net.srv.ConnectionsImpl;

public class BGSProtocol implements BidiMessagingProtocol<Message> {

    Connections<Message>        connections;
    private int                 connectionId;

    public BGSProtocol(){
        this.connections = null;
        this.connectionId = -1;
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message) {
        if(message.opcode == Message.MessageCode.REGISTER.OPCODE){
            RegisterMsg registerMsg = (RegisterMsg) message;
        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
