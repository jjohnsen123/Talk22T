import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerOutputThread extends Thread {

    Socket inputSocket;
    BufferedReader inFromUser;
    DataOutputStream outToClient;

    public ServerOutputThread(Socket inputSocket, BufferedReader inFromUser, DataOutputStream outToClient) {
        this.inputSocket = inputSocket;
        this.inFromUser = inFromUser;
        this.outToClient = outToClient;
    }

    public void run() {
        try {
            while (true) {
                outToClient.writeBytes(inFromUser.readLine() + '\n');
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
