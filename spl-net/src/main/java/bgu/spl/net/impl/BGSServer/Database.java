package bgu.spl.net.impl.BGSServer;

import java.util.LinkedList;
import java.util.List;

public class Database {

    public class userDB{
        public String username;
        public String password;
        public String birthday;
        public boolean logged_in;
    }

    List<userDB> users;

    public Database(){
        this.users = new LinkedList<>();
    }

    public void insert(String username, String password, String birthday){}

    public void delete(String username){}

    public userDB search(String username){
        return null;
    }

    public void update(userDB pre, userDB post){}
}
