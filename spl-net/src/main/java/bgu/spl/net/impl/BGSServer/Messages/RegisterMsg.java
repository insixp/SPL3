package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

public class RegisterMsg extends Message{
    public String  username;
    public String  password;
    public String  birthday;

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
        return this.StringtoByte(shortToString(opcode) + this.username + '\0' + this.password + '\0' + this.birthday + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.username = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else if(this.content_index == 1) {
            this.password = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else if(this.content_index == 2) {
            this.birthday = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }

}
