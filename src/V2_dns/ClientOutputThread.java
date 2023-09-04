package V2_dns;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientOutputThread extends Thread{
    Socket inputSocket;
    BufferedReader inFromUser;
    DataOutputStream outToServer;

    public ClientOutputThread(Socket inputSocket, BufferedReader inFromUser, DataOutputStream outToServer) {
        this.inputSocket = inputSocket;
        this.inFromUser = inFromUser;
        this.outToServer = outToServer;
    }

    public void run() {
        try {
            while (true) {
                outToServer.writeBytes(inFromUser.readLine() + '\n');
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
