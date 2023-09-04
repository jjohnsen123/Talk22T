import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TCPClient {
	public static void main(String[] args) throws Exception, IOException {
		Socket connectToChatSocket = new Socket("localhost", 1024);

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream outToServer = new DataOutputStream(connectToChatSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectToChatSocket.getInputStream()));

		outToServer.writeBytes("Vil du snakke? Ja|Nej" + '\n');
		System.out.println("Venter på svar fra den anden klient....");

		if (!inFromServer.readLine().equalsIgnoreCase("ja")) {
			System.out.println("Klient vil ikke snakke. Lukker forbindelsen");
			connectToChatSocket.close();
		}

		ClientInputThread cit = new ClientInputThread(connectToChatSocket, inFromServer);
		cit.start();
		ClientOutputThread cot = new ClientOutputThread(connectToChatSocket, inFromUser, outToServer);
		cot.start();
		System.out.println("Klienten vil gerne snakke. Du kan nu skrive til den anden");
	}
}
