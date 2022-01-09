package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class StatsMsg extends Message{
    public String  usernames;

    public StatsMsg(){
        super(MessageCode.STATS.OPCODE);
    }

    public void process(){
        User user = db.search(connId);
        AckMsg ackMsg = new AckMsg();
        ackMsg.setMsgOpCode(this.opcode);
        LinkedList<String> information = new LinkedList<>();
        if(user !=null && user.getLogged_in()) {
            String[] users = StringToListofUsers(usernames);
            if (users == null) {
                this.sendError();
            }
            else{
                for (int i = 0; i < users.length; i++) {
                    User currUser = db.get(users[i]);
                    if (currUser != null && !user.isBlocked(currUser.getUsername())) {
                        if (!information.isEmpty()) {
                            information.add(shortToString(MessageCode.ACK.OPCODE));
                            information.add(shortToString(MessageCode.STATS.OPCODE));
                        }
                        information.add(shortToString(currUser.getAge()));
                        information.add(shortToString(currUser.getNumOfPosts()));
                        information.add(shortToString(currUser.getNumberofFollowers()));
                        information.add(shortToString(currUser.getNumberofFollowing()));

                    } else {
                        this.sendError();
                        return;
                    }
                }
                ackMsg.setOptional(information);
                this.connections.send(this.connId, ackMsg);
            }

        }
        else{
            this.sendError();
        }
    }


    public String[] StringToListofUsers(String names){
        return names.split("\\|");
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.usernames + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.usernames = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }


}
