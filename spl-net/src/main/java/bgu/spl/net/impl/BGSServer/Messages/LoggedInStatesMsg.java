package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.util.LinkedList;

public class LoggedInStatesMsg extends Message{

    public LoggedInStatesMsg(){
        super(MessageCode.LOGGED_IN_STATS.OPCODE);
    }

    public void process(){
        User user=db.search(connId);
        LinkedList<String> information = new LinkedList<>();
        AckMsg ackMsg = new AckMsg();
        ackMsg.setMsgOpCode(this.opcode);
        if(user!=null&&user.getLogged_in()){
            LinkedList<User> users=db.getUsersList();
            for(int i=0;i<users.size();i++) {
                User tempuser = users.get(i);
                if (tempuser.getLogged_in()&&!user.getBlockedMeList().contains(tempuser.getUsername())) {
                    if(i>0){
                        information.add("10");
                        information.add("7");
                    }
                    information.add(tempuser.getAge());
                    information.add("" + tempuser.getNumOfPosts());
                    information.add("" + tempuser.getNumberofFollowers());
                    information.add("" + tempuser.getNumberofFollowing());
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
