package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.PostMsg;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
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
    private LinkedList<String> BlockedMeList;
    private Queue<PostMsg> backupMesseges;
    ReentrantLock followMeLock=new ReentrantLock();
    ReentrantLock IfollowLock=new ReentrantLock();
    ReentrantLock blockLock=new ReentrantLock();
    public User(){
        this.UsersIFollowList=new LinkedList<>();
        this.FollowMeList=new LinkedList<>();
        this.BlockedMeList=new LinkedList<>();
        this.backupMesseges=new LinkedBlockingQueue<>();
        this.numOfPosts=0;
    }
    public void updatePosts(){this.numOfPosts++;}

    public Queue<PostMsg> getBackupMesseges() {
        return backupMesseges;
    }
    public LinkedList<String> getUsersIFollowList(){
        LinkedList<String> copy=new LinkedList<>();
        IfollowLock.lock();
        for(int i=0;i<this.UsersIFollowList.size();i++){
            copy.add(UsersIFollowList.get(i));
             }
        IfollowLock.unlock();
        return copy;
        }
    public LinkedList<String> getBlockedMeList(){
        LinkedList<String> copy=new LinkedList<>();
        blockLock.lock();
        for(int i=0;i<this.BlockedMeList.size();i++){
            copy.add(BlockedMeList.get(i));
        }
        blockLock.unlock();
        return copy;
    }
    public LinkedList<String> getFollowMeList(){
        LinkedList<String> copy=new LinkedList<>();
        followMeLock.lock();
        for(int i=0;i<this.FollowMeList.size();i++){
            copy.add(FollowMeList.get(i));
        }
        followMeLock.unlock();
        return copy;
    }
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
    public void addToBlockedMe(String username){this.BlockedMeList.add(username);}
    public void removeBlockedMe(String username){this.BlockedMeList.remove(username);}
    public void pushBackup(PostMsg post){this.backupMesseges.add(post);}

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getAge(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthday = LocalDate.parse(this.birthday, format);
        return Period.between(birthday , LocalDate.now()).getYears();
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
