package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class TPCMain {

    public static void main(String[] args) {
        try (Server<Message> server = Server.threadPerClient(new Integer(args[0]), BGSProtocol::new, BGSEncDec::new)){
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
