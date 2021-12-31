package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class ReactorMain {

    public static void main(String[] args) {
        try (Server<String> server = Server.reactor(50, 7777, EchoProtocol::new, LineMessageEncoderDecoder::new)){
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
