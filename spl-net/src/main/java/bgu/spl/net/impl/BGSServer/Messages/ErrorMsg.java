package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.io.IOException;

public class ErrorMsg extends Message{
    private short    msgOpCode;

    private byte[] buff;

    public ErrorMsg(){
        super(MessageCode.ERROR.OPCODE);
    }

    public void setOpCode(short msgOpCode) { this.msgOpCode = msgOpCode; }

    public short getMsgOpCode() { return msgOpCode; }

    public void process() throws IOException{
        throw new IOException("Recieved Error msg inside the server");
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + shortToString(this.msgOpCode));
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.buff = new byte[2];
            this.buff[0] = data;
            this.content_index++;
        }
        else if(this.content_index == 1){
            this.buff[1] = data;
            this.msgOpCode = this.bytesToShort(this.buff);
        }
        else {
            this.data.add(data);
        }
    }


}
