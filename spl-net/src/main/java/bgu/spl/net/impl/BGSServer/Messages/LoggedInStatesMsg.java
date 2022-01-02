package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.Database;

public class LoggedInStatesMsg extends Message{

    public LoggedInStatesMsg(){
        super(MessageCode.LOGGED_IN_STATS.OPCODE);
    }

    public Message process(Database db){
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
