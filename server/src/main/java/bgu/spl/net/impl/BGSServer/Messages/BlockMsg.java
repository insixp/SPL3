package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.io.IOException;

public class BlockMsg extends Message{
    private String  username;

    public BlockMsg(){
        super(MessageCode.BLOCK.OPCODE);
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public void process() {
        User blockedUser = db.get(username);
        User blockingUser = db.search(connId);
        if(blockedUser != null && blockingUser != null && blockedUser != blockingUser && !blockingUser.isBlocked(this.username)){
            String blockingName = blockingUser.getUsername();
            blockedUser.addToBlockedMe(blockingName);
            blockingUser.addToIBlocked(username);
            blockedUser.removeFollowMe(blockingName);
            blockedUser.removeUsersIFollow(blockingName);
            blockingUser.removeFollowMe(username);
            blockingUser.removeUsersIFollow(username);
            AckMsg ackMsg = new AckMsg();
            ackMsg.setMsgOpCode(opcode);
            this.connections.send(this.connId, ackMsg);
        }
        else{
            this.sendError();
        }

    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.username = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }
}
