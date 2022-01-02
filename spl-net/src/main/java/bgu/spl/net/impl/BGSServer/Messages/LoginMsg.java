package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.nio.charset.StandardCharsets;

public class LoginMsg extends Message{
    public String  username;
    public String  password;
    public byte    captcha;

    public LoginMsg(){
        super(MessageCode.LOGIN.OPCODE);
    }


    public void process(){
        User user = db.get(this.username);
        if(user == null){
            this.sendError();
        } else {
            if(!user.getPassword().equals(this.password)){
                this.sendError();
            } else {
                user.setLogged_in(true);
                user.setConnectionID(this.connId);
                AckMsg ackMsg = new AckMsg();
                this.connections.send(connId, ackMsg);
            }
        }
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + '\0' + this.password + '\0' + this.captcha);
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
            this.captcha = data;
        }
        else {
            this.data.add(data);
        }
    }


}
