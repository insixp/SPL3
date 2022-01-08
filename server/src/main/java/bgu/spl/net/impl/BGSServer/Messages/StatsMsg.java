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
        User user=db.search(connId);
        AckMsg ackMsg = new AckMsg();
        ackMsg.setMsgOpCode(this.opcode);
        LinkedList<String> information = new LinkedList<>();
        boolean eror=false;
        if(user!=null&&user.getLogged_in()) {
            LinkedList<User> users = StringToListofUsers(usernames);
            if (users == null) {
                this.sendError();
            }
            else{
                for (int i = 0; i < users.size(); i++) {
                    User tempuser = users.get(i);
                    if (tempuser != null &&!user.isBlock(tempuser.getUsername())) {
                            if (i > 0) {
                                information.add("10");
                                information.add("7");
                            }
                            information.add(tempuser.getAge());
                            information.add("" + tempuser.getNumOfPosts());
                            information.add("" + tempuser.getNumberofFollowers());
                            information.add("" + tempuser.getNumberofFollowing());

                    } else {
                        this.sendError();
                        eror=true;
                    }
                }
                if(!eror) {
                    ackMsg.setOptional(information);
                    this.connections.send(this.connId, ackMsg);
                }
            }

        }
        else{
            this.sendError();
        }
    }


    public LinkedList<User> StringToListofUsers(String names){
        LinkedList<User> ans=new LinkedList<>();
        String usernames=names;
        if (names.charAt(names.length()-1)!='|')
            usernames=usernames+"|";
        int nextBarack=0;
        nextBarack=usernames.indexOf("|");
        while(nextBarack!=-1){
            String name=usernames.substring(0,nextBarack);
            User user=db.get(name);
            if(user==null)
                return null;
            ans.add(user);
            usernames=usernames.substring(nextBarack+1);
            nextBarack=usernames.indexOf("|");
        }
        return ans;
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
