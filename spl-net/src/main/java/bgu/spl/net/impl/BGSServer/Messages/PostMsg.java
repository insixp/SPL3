package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class PostMsg extends Message{
    public String  content;

    public PostMsg(){
        super(MessageCode.POST.OPCODE);
    }

    public void process(){}

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.content + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.content = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }

}
