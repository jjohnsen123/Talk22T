import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class DomainNameServer {
    public static void main(String[] args) throws IOException {
        DomainNameServer dns = new DomainNameServer();
        dns.records.put("localhost", "localhost");

        ServerSocket dnsSocket = new ServerSocket(1025);

        while (true) {
            Socket connectionSocket = dnsSocket.accept();
            System.out.println("Connection from: " + connectionSocket);
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            DomainNameServerThread dnsT = new DomainNameServerThread(dns, connectionSocket, inFromClient, outToClient);
            dnsT.start();
        }
    }
    public HashMap<String, String> records = new HashMap<>();
    public DomainNameServer() {
    }
}
