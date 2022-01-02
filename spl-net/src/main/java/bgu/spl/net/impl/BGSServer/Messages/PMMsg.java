package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class PMMsg extends Message{

    public String  username;
    public String  content;
    public String  info;

    public PMMsg(){
        super(MessageCode.PM.OPCODE);
    }

    public void process(){}

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + '\0' + this.content + '\0' + this.info + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.username = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else if(this.content_index == 2) {
            this.content = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else if(this.content_index == 4) {
            this.info = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }


}
