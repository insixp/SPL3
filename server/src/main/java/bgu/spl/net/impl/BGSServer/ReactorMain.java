package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class ReactorMain {

    public static void main(String[] args) {
        try (Server<Message> server = Server.reactor(new Integer(args[0]), new Integer(args[1]), BGSProtocol::new, BGSEncDec::new)){
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
