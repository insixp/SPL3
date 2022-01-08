package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.ConnectionHandler;
import bgu.spl.net.impl.BGSServer.Messages.NotificationMsg;
import bgu.spl.net.impl.BGSServer.Messages.PostMsg;
import bgu.spl.net.impl.BGSServer.Messages.SavedMessegesData;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Database {

    ConcurrentHashMap<String, User>  usernameToUserDBHS;
    SavedMessegesData SavedMesseges;
    //ReentrantLock FollowList=new ReentrantLock();   ask CHUPA
    private String[] filterWords={"goku","gohan","mkita"};

    private static class singletonHolder{
        private static Database instance = new Database();
    }

    public static Database getInstance(){
        return singletonHolder.instance;
    }

    List<User> users;

    private Database(){
        this.users = new LinkedList<>();
        this.usernameToUserDBHS = new ConcurrentHashMap<>();
        this.SavedMesseges=new SavedMessegesData();
    }

    public void register(String username, String password, String birthday){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setBirthday(birthday);
        user.setConnectionID(-1);
        user.setLogged_in(false);
        this.usernameToUserDBHS.put(username, user);
    }

    public void delete(String username){
        this.usernameToUserDBHS.remove(username);
    }

    public User get(String username){
        return this.usernameToUserDBHS.get(username);
    }

    public User search(int connId){
        List<User>  users = Collections.list(this.usernameToUserDBHS.elements());
        int counter =0;
        for(User user : users){
            counter++;
            if(user.getConnectionID() == connId)
                return user;
            if(counter>3)
                return null;
        }
        return null;
    }
    public String[] getFilterWords(){return this.filterWords;}
    public void savePMMesssege(NotificationMsg noti){
        this.SavedMesseges.insertPM(noti);
    }
    public void savePostMessege(NotificationMsg post){
        this.SavedMesseges.insertPost(post);
    }
    public LinkedList<User> getUsersList(){
        return new LinkedList<>(Collections.list(this.usernameToUserDBHS.elements()));}
}
