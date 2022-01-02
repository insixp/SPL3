package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class FollowUnfollowMsg extends Message{
    public byte         action;
    public String       username;

    public FollowUnfollowMsg(){
        super(MessageCode.FOLLOW.OPCODE);
    }

    public void process(){
        if(action==0)
            processFollow();
        else
            processUnfollow();
    }
    public void processFollow(){
        User user=db.search(this.connId);
        LinkedList<String> folowers=user.getFollowList();
        if(user.getLogged_in()&& !folowers.contains(this.username)){
            folowers.add(username);
            AckMsg ackMsg=new AckMsg();
            ackMsg.setMsgOpCode(opcode);
            ackMsg.setFollowedUsername(this.username);
            connections.send(connId,ackMsg);
        }
        else{
            this.sendError();
        }
    }
    public void processUnfollow(){
        User user=db.search(this.connId);
        LinkedList<String> folowers=user.getFollowList();
        if(user.getLogged_in()&& folowers.contains(this.username)){
            folowers.remove(username);
            AckMsg ackMsg=new AckMsg();
            ackMsg.setMsgOpCode(opcode);
            ackMsg.setFollowedUsername(this.username);
            connections.send(connId,ackMsg);
        }
        else{
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
