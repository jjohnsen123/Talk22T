package V1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TCPClient {

	public static void main(String[] args) throws Exception, IOException {
		Socket connectToChatSocket = new Socket("localhost", 1024);

		String clientMsg;

		while (true) {
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream outToServer = new DataOutputStream(connectToChatSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectToChatSocket.getInputStream()));
			outToServer.writeBytes(inFromUser.readLine() + '\n');
			clientMsg = inFromServer.readLine();
			System.out.println(clientMsg);
		}
	}

}
