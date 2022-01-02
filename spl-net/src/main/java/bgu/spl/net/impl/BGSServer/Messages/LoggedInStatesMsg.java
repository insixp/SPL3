package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

public class LoggedInStatesMsg extends Message{

    public LoggedInStatesMsg(){
        super(MessageCode.LOGGED_IN_STATS.OPCODE);
    }

    public void process(){}

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode));
    }

    @Override
    public void decodeNextByte(byte data) {
        this.data.add(data);
    }


}
