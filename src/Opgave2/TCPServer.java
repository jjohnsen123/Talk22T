package Opgave2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = new ServerSocket(1024); // Serveren lytter på port 1024

		while (true) {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Forbindelse fra: " + clientSocket);

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

			// Serveren tager initiativ til dialogen
			outToClient.writeBytes("Vil du snakke med mig? (ja/nej)\n");
			String response = inFromClient.readLine();

			if (response != null && response.equalsIgnoreCase("ja")) {
				outToClient.writeBytes("Dialogen er startet. Du kan begynde.\n");

				while (true) {
					String serverMessage = new BufferedReader(new InputStreamReader(System.in)).readLine();
					outToClient.writeBytes(serverMessage + '\n');

					String clientResponse = inFromClient.readLine();
					System.out.println("Klientens svar: " + clientResponse);

					if (serverMessage.equalsIgnoreCase("afslut") || clientResponse.equalsIgnoreCase("afslut")) {
						break; // Afslut dialogen
					}
				}

				outToClient.writeBytes("Dialogen er afsluttet. Farvel!\n");
			} else {
				outToClient.writeBytes("Dialogen blev ikke accepteret. Farvel!\n");
			}

			clientSocket.close();
		}
	}
}
