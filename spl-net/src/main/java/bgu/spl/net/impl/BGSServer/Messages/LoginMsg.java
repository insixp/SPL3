package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.Database;

import java.nio.charset.StandardCharsets;

public class LoginMsg extends Message{
    public String  username;
    public byte    pad;
    public String  password;
    public byte    pad2;
    public byte    captcha;

    public LoginMsg(){
        super(MessageCode.LOGIN.OPCODE);
    }


    public Message process(Database db){
        return null;
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + this.pad + this.password + this.pad2 + this.captcha);
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
            this.password = this.bytesToString(data);
            if(data == '\n')
                this.content_index++;
        }
        else if(this.content_index == 3){
            this.pad2 = data;
        }
        if(this.content_index == 2) {
            this.captcha = data;
        }
        else {
            this.data.add(data);
        }
    }


}
