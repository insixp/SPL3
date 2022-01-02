package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class PostMsg extends Message{
    public String  content;
    public byte    pad;

    public PostMsg(){
        super(MessageCode.POST.OPCODE);
    }

    public Message process(Database db){
        return null;
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.content + this.pad);
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.content = this.bytesToString(data);
            if(data == '\n')
                this.content_index++;
        }
        else if(this.content_index == 1){
            this.pad = data;
        }
        else {
            this.data.add(data);
        }
    }

}
