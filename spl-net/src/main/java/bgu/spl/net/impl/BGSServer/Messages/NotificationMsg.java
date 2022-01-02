package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class NotificationMsg extends Message{

    public byte     action;
    public String   username;
    public byte     pad1;
    public String   content;
    public byte     pad2;

    public NotificationMsg(){
        super(MessageCode.NOTIFICATION.OPCODE);
        this.pad1 = '\0';
        this.pad2 = '\0';
    }

    public void process(Database db, Connections<Message> connections, int connId){}

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + byteToString(this.action) + this.username + byteToString(this.pad1) + this.content + byteToString(this.pad2));
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.action = data;
        }
        if(this.content_index == 1) {
            this.username = this.bytesToString(data);
            if(data == '\n')
                this.content_index++;
        }
        else if(this.content_index == 2){
            this.pad1 = data;
        }
        if(this.content_index == 3) {
            this.content = this.bytesToString(data);
            if(data == '\n')
                this.content_index++;
        }
        else if(this.content_index == 4){
            this.pad2 = data;
        }
        else {
            this.data.add(data);
        }
    }


}
