import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DomainNameServerThread extends Thread {
    private DomainNameServer dns;
    private Socket connectionSocket;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private String job;

    public DomainNameServerThread(DomainNameServer dns, Socket connectionSocket, BufferedReader inFromClient, DataOutputStream outToClient) {
        this.dns = dns;
        this.connectionSocket = connectionSocket;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    public void run() {
        while (true) {
            try {
                String[] input = inFromClient.readLine().toLowerCase().split(" ");
                job = input[1];
                if (job.equals("get")) {
                    if (dns.records.containsKey(input[0])) {
                        String outRecord = dns.records.get(input[0]);
                        outToClient.writeBytes(outRecord + '\n');
                    } else {
                        outToClient.writeBytes(input[0] + " findes ikke i DNS'en" + '\n');
                    }
                } else if (job.equals("list")) {
                    dns.records.forEach((key, value) -> {
                        try {
                            outToClient.writeBytes("Navn: " + key + " adresse: " + value + '\n');
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    outToClient.writeBytes("end" + '\n');
                } else if (job.equals("add")) {
                    System.out.println("Hit");
                    dns.records.put(input[0], String.valueOf(connectionSocket.getInetAddress()).substring(1));
                    outToClient.writeBytes("Tilføjet" + '\n');
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
