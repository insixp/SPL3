package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Database;
import bgu.spl.net.impl.BGSServer.User;

import java.nio.charset.StandardCharsets;

import static java.lang.Character.isLetter;

public class PMMsg extends Message{

    private String  username;
    private String  content;
    private String  info;

    public String getUsername() { return username; }
    public String getContent() { return content; }
    public String getInfo() { return info; }

    public void setUsername(String username) { this.username = username; }
    public void setContent(String content) { this.content = content; }
    public void setInfo(String info) { this.info = info; }

    public PMMsg(){
        super(MessageCode.PM.OPCODE);
    }

    public void process(){
        User sendUser = db.search(this.connId);
        User receiveUser = db.get(username);
        if(sendUser != null && receiveUser != null && sendUser != receiveUser && sendUser.getLogged_in() &&
                sendUser.isFollowing(this.username) &&
                !sendUser.isBlocked(username)){
            int receiveID = receiveUser.getConnectionID();
            NotificationMsg noti = new NotificationMsg();
            noti.setContent(filter(content));
            noti.setUsername(sendUser.getUsername());
            AckMsg ackmsg = new AckMsg();
            ackmsg.setMsgOpCode(this.opcode);
            this.connections.send(this.connId,ackmsg);
            this.connections.send(receiveID,noti);
            this.db.savePMMesssege(noti);
        }
        else{
            this.sendError();
        }

    }

    @Override
    public byte[] serialize() {
        return this.StringtoByte(shortToString(opcode) + this.username + '\0' + this.content + '\0' + this.info + '\0');
    }

    @Override
    public void decodeNextByte(byte data) {
        if(this.content_index == 0) {
            this.username = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else if(this.content_index == 1) {
            this.content = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else if(this.content_index == 2) {
            this.info = this.bytesToString(data);
            if(data == '\0')
                this.content_index++;
        }
        else {
            this.data.add(data);
        }
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
