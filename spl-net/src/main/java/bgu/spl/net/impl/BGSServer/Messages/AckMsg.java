package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class AckMsg extends Message{
    public short    msgOpCode;

    public AckMsg(){
        super(MessageCode.ACK.OPCODE);
    }

    @Override
    public void process() throws IOException{
        throw new IOException("Recieved Ack msg inside the server");
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public void decodeNextByte(byte data) {
        this.data.add(data);
    }

}
