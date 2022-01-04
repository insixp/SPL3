package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class ReactorMain {

    public static void main(String[] args) {
        try (Server<Message> server = Server.reactor(50, 7777, BGSProtocol::new, BGSEncDec::new)){
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
