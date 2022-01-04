package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class Message {
    protected short  opcode;
    protected LinkedList<Byte> data;
    protected int content_index;
    protected byte[] buff = new byte[64];
    private int len;
    protected Connections<Message> connections;
    protected Database db;
    protected int connId;

    //  OPCODES
    public enum MessageCode {
        REGISTER(1),
        LOGIN(2),
        LOGOUT(3),
        FOLLOW(4),
        POST(5),
        PM(6),
        LOGGED_IN_STATS(7),
        STATS(8),
        NOTIFICATION(9),
        ACK(10),
        ERROR(11),
        BLOCK(12);

        public final short OPCODE;
        private MessageCode(int opcode){ this.OPCODE = (short)opcode; }
    }

    public Message(short opcode){
        this.opcode = opcode;
        this.content_index = 0;
        this.len = 0;
        this.data = new LinkedList<Byte>();
    }

    public void setConnections(Connections<Message> connections) {
        this.connections = connections;
    }

    public void setDb(Database db) {
        this.db = db;
    }

    public void setConnId(int connId) {
        this.connId = connId;
    }

    public abstract void process() throws IOException;
    public abstract byte[] serialize();
    public abstract void   decodeNextByte(byte data);

    protected String bytesToString(byte data){
        if (data == '\0') {
            String result = new String(buff, 0, len, StandardCharsets.UTF_8);
            len = 0;
            return result;
        }

        if (len >= this.buff.length) {
            this.buff = Arrays.copyOf(this.buff, len * 2);
        }
        this.buff[len++] = data;
        return null;
    }

    protected void sendError(){
        ErrorMsg replyError = new ErrorMsg();
        replyError.setOpCode(this.opcode);
        connections.send(connId, replyError);
    }

    protected byte[] StringtoByte(String data){
        return data.getBytes(StandardCharsets.UTF_8);
    }
    protected String shortToString(short data){
        return new String(shortToBytes(data), 0, 2, StandardCharsets.UTF_8);
    }
    protected short stringToShort(String data){
        return bytesToShort(StringtoByte(data));
    }
    protected String byteToString(byte data){
        return new String(new byte[]{data}, 0, 1, StandardCharsets.UTF_8);
    }
    protected byte stringToByte(String data){
        return StringtoByte(data)[0];
    }
    protected short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
    protected byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
