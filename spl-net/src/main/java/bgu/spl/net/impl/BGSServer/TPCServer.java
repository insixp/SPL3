package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class TPCServer {

    public static void main(String[] args) {
        try (Server<String> server = Server.threadPerClient(Integer.decode(args[0]), EchoProtocol::new, LineMessageEncoderDecoder::new)){
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
