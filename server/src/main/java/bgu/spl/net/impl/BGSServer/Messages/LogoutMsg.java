package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

public class LogoutMsg extends Message{

    public LogoutMsg(){
        super(MessageCode.LOGOUT.OPCODE);
    }

    @Override
    public void process(){
        User user = this.db.search(this.connId);
        if(user == null){
            this.sendError();
        } else {
            user.setLogged_in(false);
            user.setConnectionID(-1);
            AckMsg ackMsg = new AckMsg();
            ackMsg.setMsgOpCode(this.opcode);
            this.connections.send(this.connId, ackMsg);
        }
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode));
    }

    @Override
    public void decodeNextByte(byte data) {
        this.data.add(data);
    }


}
