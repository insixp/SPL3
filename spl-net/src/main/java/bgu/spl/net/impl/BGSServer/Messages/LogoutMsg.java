package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

public class LogoutMsg extends Message{

    public LogoutMsg(){
        super(MessageCode.LOGOUT.OPCODE);
    }

    @Override
    public void process(Database db, Connections<Message> connections, int connId){}

    @Override
    public byte[] serialize() {
        return null;
    }

    @Override
    public void decodeNextByte(byte data) {
        this.data.add(data);
    }


}
