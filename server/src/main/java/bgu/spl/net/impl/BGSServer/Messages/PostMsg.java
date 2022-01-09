package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.srv.ConnectionsImpl;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Character.isLetter;

public class PostMsg extends Message{
    public String  content;

    public PostMsg(){
        super(MessageCode.POST.OPCODE);
    }

    public void process(){
        User user=db.search(this.connId);
        if(user.getLogged_in()){
        NotificationMsg myPost=new NotificationMsg();
        myPost.setContent(filter(content));
        myPost.setUsername(user.getUsername());
        ConcurrentLinkedQueue<String> followMe=user.getFollowMeList();
        LinkedList<Integer> ShtrudelUsers=findShtrudel(content);
            while(!followMe.isEmpty()){
                User follower = db.get(followMe.poll());
                if (follower.getLogged_in()) {
                    this.connections.send(follower.getConnectionID(),myPost);
                } else {//if the user is logout
                    follower.pushBackup(myPost);
                }
            }
        for(int j=0;j<ShtrudelUsers.size();j++){//all the @users
            this.connections.send(ShtrudelUsers.get(j),myPost);
        }
        AckMsg ackmsg=new AckMsg();
        ackmsg.setMsgOpCode(this.opcode);
        this.connections.send(this.connId,ackmsg);
        user.updatePosts();
        this.db.savePostMessege(myPost);
    }
        else{
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
    private LinkedList<Integer> findShtrudel(String content){
        User Postuser=db.search(this.connId);
        LinkedList<Integer> ans=new LinkedList<>();
        String content1=content;
        String name="";
        int firstShtrud=content1.indexOf("@");
        int temp=firstShtrud;
        while(temp!=-1){
            int endname=content1.substring(firstShtrud).indexOf(" ");
            if(endname==-1){
                endname=content1.length()-firstShtrud;
            }
            name=content1.substring(firstShtrud+1,firstShtrud+endname);
            User user=db.get(name);
            if(user!=null &&!Postuser.isBlock(name))
                ans.add(user.getConnectionID());
            temp=content1.substring(firstShtrud+endname).indexOf("@");
            firstShtrud=firstShtrud+temp+name.length()+1;

        }
        return ans;
    }

    private String filter(String content){
        if(content!=null) {
            String[] filterWords = db.getFilterWords();
            String s = content;
            for (int i = 0; i < filterWords.length; i++) {
                int place = s.indexOf(filterWords[i]);
                int tempPlace = place;
                int length = filterWords[i].length();
                while (tempPlace != -1) {
                    if (place == 0 || !isLetter(s.charAt(place-1))) {
                        if (s.length() <= (place + length) || !isLetter(s.charAt(place+length))) {
                            s = s.substring(0, place) + "<filter>" + s.substring(place + length);
                            length = 8;
                        }
                    }
                    tempPlace = s.substring(place + length).indexOf(filterWords[i]);
                    place = place + length + tempPlace;
                    length = filterWords[i].length();

                }
            }
            return s;
        }
        return "";
    }
}
