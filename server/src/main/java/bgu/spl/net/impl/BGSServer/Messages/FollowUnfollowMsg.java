package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FollowUnfollowMsg extends Message{
    public byte         action;
    public String       username;

    public FollowUnfollowMsg(){
        super(MessageCode.FOLLOW.OPCODE);
    }

    public void process(){
        if(action == '\0') {
            processFollow();
        }
        else
            processUnfollow();
    }
    private void processFollow(){
        User MeUser=db.search(this.connId);
        User FoUser=db.get(username);
        ConcurrentLinkedQueue<String> folowers=MeUser.getUsersIFollowList();
        if(FoUser!=null&& MeUser.getLogged_in() && !folowers.contains(this.username)&&
        !MeUser.isBlock(username)){
            MeUser.addToUsersIFollow(username);
            FoUser.addToFollowMe(MeUser.getUsername());
            AckMsg ackMsg=new AckMsg();
            ackMsg.setMsgOpCode(opcode);
            LinkedList<String>U=new LinkedList<>();
            U.add(username);
            ackMsg.setOptional(U);
            connections.send(connId,ackMsg);
        }
        else{
            this.sendError();
        }
    }
    private void processUnfollow() {
        User MeUser = db.search(this.connId);
        User FoUser = db.get(username);
        ConcurrentLinkedQueue<String> folowers = MeUser.getUsersIFollowList();
        if (FoUser != null && MeUser.getLogged_in() && folowers.contains(this.username)) {
            MeUser.removeUsersIFollow(username);
            FoUser.removeFollowMe(MeUser.getUsername());
            AckMsg ackMsg = new AckMsg();
            LinkedList<String>U=new LinkedList<>();
            U.add(username);
            ackMsg.setOptional(U);
            connections.send(connId, ackMsg);
        } else {
            this.sendError();
        }
    }
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
