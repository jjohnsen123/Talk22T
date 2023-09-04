import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TCPClient {

	public static void main(String[] args) throws Exception, IOException {
		Socket connectToChatSocket = new Socket("localhost", 1024);

		String clientMsg;
		Boolean firstMsg = true;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream outToServer = new DataOutputStream(connectToChatSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectToChatSocket.getInputStream()));

		while (true) {
			if (firstMsg) {
				outToServer.writeBytes("Vil du snakke? Ja|Nej" + '\n');
				System.out.println("Venter på svar fra den anden klient....");
				firstMsg = false;
				clientMsg = inFromServer.readLine();
				if (!clientMsg.equals("Ja")) {
					System.out.println("Klient vil ikke snakke. Lukker forbindelsen");
					break;
				}
				System.out.println("Klienten vil gerne snakke. Du kan nu skrive til den anden");
			}
			outToServer.writeBytes(inFromUser.readLine() + '\n');
			clientMsg = inFromServer.readLine();
			System.out.println(clientMsg);
		}
		connectToChatSocket.close();
	}

}
