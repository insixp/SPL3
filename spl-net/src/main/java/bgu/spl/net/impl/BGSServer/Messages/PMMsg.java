package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class PMMsg extends Message{

    public String  username;
    public byte    pad;
    public String  content;
    public byte    pad2;
    public String  info;
    public byte    pad3;

    public PMMsg(){
        super(MessageCode.PM.OPCODE);
    }

    public void process(){}

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + byteToString(this.pad) + this.content + byteToString(this.pad2) + this.info + byteToString(this.pad3));
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.username = this.bytesToString(data);
            if(data == '\n')
                this.content_index++;
        }
        else if(this.content_index == 1){
            this.pad = data;
        }
        if(this.content_index == 2) {
            this.content = this.bytesToString(data);
            if(data == '\n')
                this.content_index++;
        }
        else if(this.content_index == 3){
            this.pad2 = data;
        }
        if(this.content_index == 4) {
            this.info = this.bytesToString(data);
            if(data == '\n')
                this.content_index++;
        }
        else if(this.content_index == 5){
            this.pad3 = data;
        }
        else {
            this.data.add(data);
        }
    }


}
