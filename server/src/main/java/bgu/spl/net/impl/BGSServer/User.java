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
    private ConcurrentLinkedQueue<String> UsersIFollowList;
    private ConcurrentLinkedQueue<String>FollowMeList;
    private ConcurrentLinkedQueue<String>BlockedMeList;
    private ConcurrentLinkedQueue<String>IBlockedList;
    private ConcurrentLinkedQueue<NotificationMsg>backupMesseges;

    public User(){
        this.UsersIFollowList=new ConcurrentLinkedQueue<>();
        this.FollowMeList=new ConcurrentLinkedQueue<>();
        this.BlockedMeList=new ConcurrentLinkedQueue<>();
        this.IBlockedList=new ConcurrentLinkedQueue<>();
        this.backupMesseges=new ConcurrentLinkedQueue<>();
        this.numOfPosts=0;
    }
    public void updatePosts(){this.numOfPosts++;}

    public ConcurrentLinkedQueue<NotificationMsg> getBackupMesseges() {
        return backupMesseges;
    }
    public ConcurrentLinkedQueue<String> getUsersIFollowList(){return new ConcurrentLinkedQueue(UsersIFollowList);}
    public ConcurrentLinkedQueue<String> getBlockedMeList(){return new ConcurrentLinkedQueue(BlockedMeList);}
    public ConcurrentLinkedQueue<String> getFollowMeList(){return new ConcurrentLinkedQueue(FollowMeList);}
    public String getUsername() {
        return username;
    }

    public void addToFollowMe(String username){this.FollowMeList.add(username);}
    public void removeFollowMe(String username){this.FollowMeList.remove(username);}
    public void addToUsersIFollow(String username){this.UsersIFollowList.add(username);}
    public void removeUsersIFollow(String userame){this.UsersIFollowList.remove(userame);}
    public void addToBlockedMe(String username){this.BlockedMeList.add(username);}
    public void addToIBlocked(String username){this.IBlockedList.add(username);}

    public void pushBackup(NotificationMsg post){this.backupMesseges.add(post);}

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
    public int getConnectionID() {return connectionID;}
    public boolean getLogged_in() {return this.logged_in;}

    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setBirthday(String birthday) {this.birthday = birthday;}
    public void setConnectionID(int connectionID) {this.connectionID = connectionID;}
    public void setLogged_in(boolean logged_in) {this.logged_in = logged_in;}
}
