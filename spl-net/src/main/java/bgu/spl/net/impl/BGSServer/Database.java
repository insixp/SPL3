package bgu.spl.net.impl.BGSServer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    ConcurrentHashMap<String, User>  usernameToUserDBHS;

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
        for(User user : users){
            if(user.getConnectionID() == connId)
                return user;
        }
        return null;
    }
}
