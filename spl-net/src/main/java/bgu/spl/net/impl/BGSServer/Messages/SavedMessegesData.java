package bgu.spl.net.impl.BGSServer.Messages;
import java.util.LinkedList;

public class SavedMessegesData {
    private LinkedList<NotificationMsg> PM;
    private LinkedList<PostMsg> Posts;

    public SavedMessegesData() {
        this.PM = new LinkedList<>();
        this.Posts = new LinkedList<>();
    }

    public void insertPM(NotificationMsg noti) {
        this.PM.add(noti);
    }

    public void insertPost(PostMsg post) {
        this.Posts.add(post);
    }
}
