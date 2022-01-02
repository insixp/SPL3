package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NotificationMsg extends Message{

    public byte     action;
    public String   username;
    public String   content;

    public NotificationMsg(){
        super(MessageCode.NOTIFICATION.OPCODE);
    }

    public void process() throws IOException{
        throw new IOException("Recieved Error msg inside the server");
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + byteToString(this.action) + this.username + '\0' + this.content + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.action = data;
        }
        if(this.content_index == 1) {
            this.username = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else if(this.content_index == 2) {
            this.content = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }


}
