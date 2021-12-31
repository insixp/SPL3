package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    ConcurrentHashMap<Integer, ConnectionHandler<T>>    connectionIdHM;
    private int connectionIdIndex;


    private static class singletonHolder{
        private static ConnectionsImpl instance = new ConnectionsImpl();
    }

    public ConnectionsImpl(){
        this.connectionIdHM = new ConcurrentHashMap<Integer, ConnectionHandler<T>>();
        this.connectionIdIndex = 0;
    }

    public ConnectionsImpl getInstance(){
        return singletonHolder.instance;
    }

    public int register(ConnectionHandler<T> connectionHandler){
        int connID = this.connectionIdIndex;
        this.connectionIdHM.put(connID, connectionHandler);
        this.connectionIdIndex++;
        return connID;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler connectionHandler = this.connectionIdHM.get(connectionId);
        if(connectionHandler == null)
            return false;
        connectionHandler.send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        Queue<ConnectionHandler> sendingQueue = new LinkedList<>(Collections.list(this.connectionIdHM.elements()));
        ConnectionHandler connectionHandler;
        while(!sendingQueue.isEmpty()){
            connectionHandler = sendingQueue.poll();
            if(!connectionHandler.trySend(msg))
                sendingQueue.add(connectionHandler);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        this.connectionIdHM.remove(connectionId);
    }
}
