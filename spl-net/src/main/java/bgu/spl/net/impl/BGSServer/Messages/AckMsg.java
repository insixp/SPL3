package bgu.spl.net.impl.BGSServer.Messages;

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
    public Message process(Database db) {
        return null;
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
