package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class AckMsg extends Message{
    private short    msgOpCode;
    private String FollowedUsername="";
    public AckMsg(){
        super(MessageCode.ACK.OPCODE);
    }

    public short getMsgOpCode() {
        return msgOpCode;
    }

    public void setMsgOpCode(short msgOpCode) {
        this.msgOpCode = msgOpCode;
    }
    public void setFollowedUsername(String username){
        this.FollowedUsername=username;
    }
    @Override
    public void process() throws IOException{
        throw new IOException("Recieved Ack msg inside the server");
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + shortToString(this.msgOpCode) + FollowedUsername);
    }

    @Override
    public void decodeNextByte(byte data) {
        this.data.add(data);
    }

}
