package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.Database;

public class BlockMsg extends Message{
    public String  username;
    public byte    pad;

    public BlockMsg(){
        super(MessageCode.BLOCK.OPCODE);
    }

    public Message process(Database db){
        return null;
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + byteToString(this.pad));
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
        else {
            this.data.add(data);
        }
    }
}
