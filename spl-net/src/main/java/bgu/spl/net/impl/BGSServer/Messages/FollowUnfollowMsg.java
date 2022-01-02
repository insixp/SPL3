package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class FollowUnfollowMsg extends Message{
    public byte         action;
    public String       username;

    public FollowUnfollowMsg(){
        super(MessageCode.FOLLOW.OPCODE);
    }

    public void process(){}

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + byteToString(this.action) + this.username);
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.action = data;
            this.content_index++;
        }
        else if(this.content_index == 1){
            this.username = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }


}
