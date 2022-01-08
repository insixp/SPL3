package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.NotificationMsg;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    private String   username;
    private String   password;
    private String   birthday;
    private int      connectionID;
    private boolean  logged_in;
    private int numOfPosts;
    private ConcurrentLinkedQueue<String> UsersIFollowList;
    private ConcurrentLinkedQueue<String> FollowMeList;
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


    //  Follow
    public ConcurrentLinkedQueue<String> getFollowMeList(){ return new ConcurrentLinkedQueue(FollowMeList); }
    public void addToFollowMe(String username){this.FollowMeList.add(username);}
    public void removeFollowMe(String username){this.FollowMeList.remove(username);}
    public void addToUsersIFollow(String username){this.UsersIFollowList.add(username);}
    public void removeUsersIFollow(String userame){this.UsersIFollowList.remove(userame);}
    public boolean isFollowing(String username){ return UsersIFollowList.contains(username); }

    //  Block
    public void addToBlockedMe(String username){this.BlockedMeList.add(username);}
    public void addToIBlocked(String username){this.IBlockedList.add(username);}
    public boolean isBlocked(String username){ return this.IBlockedList.contains(username); }

    //  Backup
    public ConcurrentLinkedQueue<NotificationMsg> getBackupMesseges() {
        return backupMesseges;
    }
    public void pushBackup(NotificationMsg post){this.backupMesseges.add(post);}

    //  Posts
    public void updatePosts(){ this.numOfPosts++; }

    //  Getters
    public String getBirthday() {
        return birthday;
    }
    public String getPassword() { return this.password; }
    public String getUsername() {
        return username;
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

    //  Setters
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setBirthday(String birthday) {this.birthday = birthday;}
    public void setConnectionID(int connectionID) {this.connectionID = connectionID;}
    public void setLogged_in(boolean logged_in) {this.logged_in = logged_in;}
}
