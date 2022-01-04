package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.BGSServer.Messages.PostMsg;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class User {
    private String   username;
    private String   password;
    private String   birthday;
    private int      connectionID;
    private boolean  logged_in;
    private int numOfPosts;
    private LinkedList<String> UsersIFollowList;
    private LinkedList<String> FollowMeList;
    private Queue<PostMsg> backupMesseges;
    ReentrantLock followMeLock=new ReentrantLock();
    ReentrantLock IfollowLock=new ReentrantLock();
    public User(){
        this.UsersIFollowList=new LinkedList<>();
        this.FollowMeList=new LinkedList<>();
        this.backupMesseges=new LinkedBlockingQueue<>();
        this.numOfPosts=0;
    }
    public void updatePosts(){this.numOfPosts++;}

    public Queue<PostMsg> getBackupMesseges() {
        return backupMesseges;
    }
    public LinkedList<String> getUsersIFollowList(){return this.UsersIFollowList;}

    public LinkedList<String> getFollowMeList(){return this.FollowMeList;}

    public String getUsername() {
        return username;
    }

    public void addToFollowMe(String username){
        followMeLock.lock();
        this.FollowMeList.add(username);
        followMeLock.unlock();}

    public void removeFollowMe(String username){
        followMeLock.lock();
        this.FollowMeList.remove(username);
        followMeLock.unlock();}

    public void addToUsersIFollow(String username){
        IfollowLock.lock();
        this.UsersIFollowList.add(username);
        IfollowLock.unlock();}

    public void removeUsersIFollow(String userame){
        IfollowLock.lock();
        this.UsersIFollowList.remove(userame);
        IfollowLock.unlock();}

    public void pushBackup(PostMsg post){this.backupMesseges.add(post);}

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAge(){
        String birthYear=this.birthday.substring(6);
        int year=0;
        int temp=1;
        int Age;
        for(int i=0;i<4;i++){
            year=year+(birthYear.charAt(3-i)-48)*temp;
            temp=temp*10;
        }
        Age=2021-year;
        return ""+Age;
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public int getNumberofFollowers(){return this.FollowMeList.size();}
    public int getNumberofFollowing(){return this.UsersIFollowList.size();}
    public int getConnectionID() {
        return connectionID;
    }

    public boolean getLogged_in() {
        return this.logged_in;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }
}
