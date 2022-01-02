package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.ErrorMsg;
import bgu.spl.net.impl.BGSServer.Messages.Message;
import bgu.spl.net.impl.BGSServer.Messages.RegisterMsg;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class BGSClient {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            args = new String[]{"localhost", "hello"};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }

        BGSEncDec encdec = new BGSEncDec();

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 7777);
             BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
             BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream())) {

            System.out.println("sending message to server");
            out.write(encdec.encode(new ErrorMsg()));
            out.flush();

            System.out.println("awaiting response");
            int read;
            while((read = in.read()) >= 0){
                Message m = encdec.decodeNextByte((byte)read);
                if(m != null)
                    System.out.println(m);
            }
        }
    }
}
