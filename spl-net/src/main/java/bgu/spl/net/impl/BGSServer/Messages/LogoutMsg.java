package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.Database;

public class LogoutMsg extends Message{

    public LogoutMsg(){
        super(MessageCode.LOGOUT.OPCODE);
    }

    @Override
    public Message process(Database db) {
        return null;
    }

    @Override
    public byte[] serialize() {
        return null;
    }

    @Override
    public void decodeNextByte(byte data) {
        this.data.add(data);
    }


}
