package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.io.IOException;

public class BlockMsg extends Message{
    public String  username;

    public BlockMsg(){
        super(MessageCode.BLOCK.OPCODE);
    }

    public void process() {
        User blockedUser=db.get(username);
        User blockingUser=db.search(connId);
        String blockingName=blockingUser.getUsername();
        if(blockedUser!=null){
            blockedUser.addToBlockedMe(blockingName);
            blockedUser.removeFollowMe(blockingName);
            blockedUser.removeUsersIFollow(blockingName);
            blockingUser.removeFollowMe(username);
            blockingUser.removeUsersIFollow(username);
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
