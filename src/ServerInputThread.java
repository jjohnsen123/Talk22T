import java.io.BufferedReader;
import java.net.Socket;

public class ServerInputThread extends Thread{
    Socket inputSocket;
    BufferedReader inFromClient;
    String clientMsg;

    public ServerInputThread(Socket inputSocket, BufferedReader inFromClient) {
        this.inputSocket = inputSocket;
        this.inFromClient = inFromClient;
    }

    public void run() {
        try {
            while (true) {
                clientMsg = inFromClient.readLine();
                if (clientMsg.equals("null")) {
                    break;
                }
                System.out.println(clientMsg);
            }
            inputSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
