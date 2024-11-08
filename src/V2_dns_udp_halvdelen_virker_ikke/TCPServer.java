package V2_dns_udp_halvdelen_virker_ikke;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String[] args) throws Exception {
		ServerSocket chatSocket = new ServerSocket(1024);

		Socket connectingClient = chatSocket.accept();
		System.out.println("Connection from: " + connectingClient);

		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectingClient.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectingClient.getOutputStream());
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		ServerInputThread sit = new ServerInputThread(connectingClient, inFromClient);
		sit.start();
		ServerOutputThread sot = new ServerOutputThread(connectingClient, inFromUser, outToClient);
		sot.start();
	}
}
