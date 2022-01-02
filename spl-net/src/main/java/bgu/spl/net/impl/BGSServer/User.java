package bgu.spl.net.impl.BGSServer;

import java.util.LinkedList;

public class User {
    private String   username;
    private String   password;
    private String   birthday;
    private int      connectionID;
    private boolean  logged_in;
    private LinkedList<String> FollowList;

    public User(){
        this.FollowList=new LinkedList<>();
    }
    public LinkedList<String> getFollowList(){return this.FollowList;}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

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
