package Opgave2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) throws Exception {

		String clientSentence;
		String serverSentence;

		ServerSocket welcomeSocket = new ServerSocket(1024);
		Socket connectionSocket = welcomeSocket.accept();
		System.out.println("Connection from: " + connectionSocket);
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		
		while(true){
			serverSentence = inFromUser.readLine();
			clientSentence = inFromClient.readLine();
			System.out.println(clientSentence);
			outToClient.writeBytes(serverSentence + '\n');
		}

	}

}
