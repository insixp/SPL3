package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.io.IOException;

public class BlockMsg extends Message{
    public String  username;

    public BlockMsg(){
        super(MessageCode.BLOCK.OPCODE);
    }

    public void process() {
        
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.username = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }
}
