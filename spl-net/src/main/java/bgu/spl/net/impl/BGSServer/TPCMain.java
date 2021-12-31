package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class TPCMain {

    public static void main(String[] args) {
        try (Server<String> server = Server.threadPerClient(7777, EchoProtocol::new, LineMessageEncoderDecoder::new)){
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
