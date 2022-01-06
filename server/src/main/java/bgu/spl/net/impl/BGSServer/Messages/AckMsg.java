package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class AckMsg extends Message{
    private short    msgOpCode;
    private LinkedList<String>optional=new LinkedList<>();
    public AckMsg(){
        super(MessageCode.ACK.OPCODE);
    }

    public short getMsgOpCode() {
        return msgOpCode;
    }

    public void setMsgOpCode(short msgOpCode) {
        this.msgOpCode = msgOpCode;
    }

    public void setOptional(LinkedList<String>l){optional=l;}
    @Override
    public void process() throws IOException{
        throw new IOException("Recieved Ack msg inside the server");
    }

    @Override
    public byte[] serialize() {
        String ans="";
        ans=ans+shortToString(opcode) + shortToString(this.msgOpCode);
        for(int i=0;i<optional.size();i++){
            ans=ans+optional.get(i);
        }
        return this.StringtoByte(ans);
    }

    @Override
    public void decodeNextByte(byte data) {
        this.data.add(data);
    }

}
