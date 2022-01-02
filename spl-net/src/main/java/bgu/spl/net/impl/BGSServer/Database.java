package bgu.spl.net.impl.BGSServer;

import javax.xml.crypto.Data;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    ConcurrentHashMap<String, userDB>  usernameToUserDBHS;

    public class userDB{
        public String   username;
        public String   password;
        public String   birthday;
        public int      connectionID;
        public boolean  logged_in;
    }

    private static class singletonHolder{
        private static Database instance = new Database();
    }

    public static Database getInstance(){
        return singletonHolder.instance;
    }

    List<userDB> users;

    private Database(){
        this.users = new LinkedList<>();
        this.usernameToUserDBHS = new ConcurrentHashMap<>();
    }

    public void register(String username, String password, String birthday){
        userDB user = new userDB();
        user.username = username;
        user.password = password;
        user.birthday = birthday;
        user.connectionID = -1;
        user.logged_in = false;
        this.usernameToUserDBHS.put(username, user);
    }

    public void delete(String username){
        this.usernameToUserDBHS.remove(username);
    }

    public userDB get(String username){
        return this.usernameToUserDBHS.get(username);
    }

}
