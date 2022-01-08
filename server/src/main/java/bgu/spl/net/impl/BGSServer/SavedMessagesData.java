package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.NotificationMsg;
import java.util.LinkedList;

public class SavedMessagesData {
    private LinkedList<NotificationMsg> PM;
    private LinkedList<NotificationMsg> Posts;

    public SavedMessagesData() {
        this.PM = new LinkedList<>();
        this.Posts = new LinkedList<>();
    }

    public void insertPM(NotificationMsg noti) {
        this.PM.add(noti);
    }

    public void insertPost(NotificationMsg post) {
        this.Posts.add(post);
    }
}
