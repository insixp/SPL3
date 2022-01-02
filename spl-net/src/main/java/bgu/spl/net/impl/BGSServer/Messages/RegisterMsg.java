package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

public class RegisterMsg extends Message{
    public String  username;
    public byte    pad;
    public String  password;
    public byte    pad2;
    public String  birthday;
    public byte    pad3;

    public RegisterMsg(){
        super(MessageCode.REGISTER.OPCODE);
    }

    public void process(){
        User user = db.get(this.username);
        if(user != null) {
            this.sendError();
        } else {
            db.register(this.username, this.password, this.birthday);
            AckMsg ackMsg = new AckMsg();
            connections.send(connId, ackMsg);
        }
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + byteToString(this.pad) + this.password + byteToString(this.pad2) + this.birthday + byteToString(this.pad3));
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
        if(this.content_index == 4) {
            this.birthday = this.bytesToString(data);
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
