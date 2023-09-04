package Opgave2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

	public static void main(String[] args) throws IOException {

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		// Erstat "localhost" og portnummeret med serverens adresse og portnummer
		Socket clientSocket = new Socket("localhost", 1024); // Tilpas porten efter din serverimplementering

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		String response = inFromServer.readLine();
		System.out.println(response);

		if (response != null && response.equalsIgnoreCase("Vil du snakke med mig? (ja/nej)")) {
			System.out.print("Svar: ");
			String sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');

			while (true) {
				String modifiedSentence = inFromServer.readLine();
				System.out.println("Fra serveren: " + modifiedSentence);

				if (modifiedSentence.equalsIgnoreCase("Dialogen er afsluttet. Farvel!") || sentence.equalsIgnoreCase("afslut")) {
					break;
				}

				System.out.print("Svar: ");
				sentence = inFromUser.readLine();
				outToServer.writeBytes(sentence + '\n');
			}
		}

		clientSocket.close();
	}
}
