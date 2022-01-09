package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.srv.ConnectionsImpl;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isLetter;

public class PostMsg extends Message{
    public String  content;

    public PostMsg(){
        super(MessageCode.POST.OPCODE);
        this.content = "";
    }

    public void process(){
        User user = db.search(this.connId);
        if(user != null && user.getLogged_in()){
            NotificationMsg myPost = new NotificationMsg();
            myPost.setAction((byte)1);
            myPost.setContent(filter(content));
            myPost.setUsername(user.getUsername());
            HashSet<String> toBeSent = new HashSet<String>();
            toBeSent.addAll(new HashSet<String>(user.getFollowMeList()));
            toBeSent.addAll(findAtUsers(content));
            for(String username : toBeSent){
                User receiver = db.get(username);
                if(receiver != null && !receiver.isBlocked(user.getUsername())) {
                    if (receiver.getLogged_in()) {
                        this.connections.send(receiver.getConnectionID(), myPost);
                    } else {
                        receiver.pushBackup(myPost);
                    }
                }
            }
            AckMsg ackmsg=new AckMsg();
            ackmsg.setMsgOpCode(this.opcode);
            this.connections.send(this.connId,ackmsg);
            user.updatePosts();
            this.db.savePostMessege(myPost);
        }  else {
            this.sendError();
        }
    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.content + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.content = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
    }
    private HashSet<String> findAtUsers(String content){
        HashSet<String> usernames = new HashSet<>();
        Pattern pattern = Pattern.compile("(\\W|^)(@)(\\w*)");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            usernames.add(matcher.group(3));
        }
        return usernames;
    }

    private String filter(String content){
        for(String filteredWord : this.db.getFilterWords()){
            content = content.replaceAll("\\b" + filteredWord + "\\b", "<filtered>");
        }
        return content;
    }
}
