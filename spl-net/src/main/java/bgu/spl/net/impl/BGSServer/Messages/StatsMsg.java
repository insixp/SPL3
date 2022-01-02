package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class StatsMsg extends Message{
    public String  usernames;
    public byte    pad;

    public StatsMsg(){
        super(MessageCode.STATS.OPCODE);
    }

    public Message process(Database db){
        return null;
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.usernames + this.pad);
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.usernames = this.bytesToString(data);
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
