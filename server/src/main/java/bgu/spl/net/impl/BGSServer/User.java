package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.BGSServer.Messages.NotificationMsg;
import bgu.spl.net.impl.BGSServer.Messages.PostMsg;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
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

    public boolean isBlock(String name){return BlockedMeList.contains(name)||IBlockedList.contains(name);}
    public String getPassword() {return password;}
    public String getBirthday() {return birthday;}
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
    public int getNumOfPosts() {return numOfPosts;}
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
