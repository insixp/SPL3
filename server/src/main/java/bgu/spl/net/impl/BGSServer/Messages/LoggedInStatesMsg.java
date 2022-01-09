package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.util.LinkedList;

public class LoggedInStatesMsg extends Message{

    public LoggedInStatesMsg(){
        super(MessageCode.LOGGED_IN_STATS.OPCODE);
    }

    @Override
    public void process(){
        User user = db.search(connId);
        AckMsg ackMsg = new AckMsg();
        ackMsg.setMsgOpCode(this.opcode);
        LinkedList<String> information = new LinkedList<>();
        if(user != null && user.getLogged_in()){
            LinkedList<User> users = db.getUsersList();
            for(int i = 0; i < users.size();i++){
                User currUser = users.get(i);
                if(currUser != null) {
                    if(currUser.getLogged_in() && !user.isBlocked(currUser.getUsername())) {
                            if (!information.isEmpty()) {
                                information.add(shortToString(MessageCode.ACK.OPCODE));
                                information.add(shortToString(MessageCode.LOGGED_IN_STATS.OPCODE));
                            }
                            information.add(shortToString(currUser.getAge()));
                            information.add(shortToString(currUser.getNumOfPosts()));
                            information.add(shortToString(currUser.getNumberofFollowers()));
                            information.add(shortToString(currUser.getNumberofFollowing()));
                        }
                }
                else{
                    this.sendError();
                }
            }
            ackMsg.setOptional(information);
            this.connections.send(this.connId, ackMsg);
        }
        else{
            this.sendError();
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
