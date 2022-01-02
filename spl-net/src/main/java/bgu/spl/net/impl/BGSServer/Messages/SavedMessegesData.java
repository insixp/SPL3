package bgu.spl.net.impl.BGSServer.Messages;
import java.util.LinkedList;

public class SavedMessegesData {
    private LinkedList<NotificationMsg> PM;
    private LinkedList<PostMsg> Posts;
    private String[] filterWords={"Gohan","Makit","Gooku"};

    public SavedMessegesData() {
        this.PM=new LinkedList<>();
        this.Posts=new LinkedList<>();
    }
    public void insertPM(NotificationMsg noti){
        this.PM.add(filter(noti));
    }
    public void insertPost(PostMsg post){
        this.Posts.add(filter(post));
    }
    private NotificationMsg filter(NotificationMsg noti){
        String content=noti.getContent();
        for(int i=0;i<filterWords.length;i++) {
            int place=content.lastIndexOf(filterWords[i]);
            while (place!=-1){
                content=content.substring(0,place)+"<filter>"+content.substring(place+5);
                place=content.lastIndexOf(filterWords[i]);
            }
        }
        return noti;
    }
    private PostMsg filter(PostMsg post){
        String content=post.getContent();
        for(int i=0;i<filterWords.length;i++) {
            int place=content.lastIndexOf(filterWords[i]);
            while (place!=-1){
                content=content.substring(0,place)+"<filter>"+content.substring(place+5);
                place=content.lastIndexOf(filterWords[i]);
            }
        }
        return post;
        }
}