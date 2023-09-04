package V2_dns;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class TCPClient2 {
	public static void main(String[] args) throws Exception {
		boolean loop = true;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket connectToDnsSocket = new Socket("localhost", 1025);
		BufferedReader inFromDnsServer = new BufferedReader(new InputStreamReader(connectToDnsSocket.getInputStream()));
		DataOutputStream outToDnsServer = new DataOutputStream(connectToDnsSocket.getOutputStream());

		while (loop) {
			System.out.println("Vælg tallet efter hvad du gerne vil");
			System.out.println("1 - Forbinde til en");
			System.out.println("2 - Se listen fra DNS'en");
			System.out.println("3 - Skrives op i DNS'en");
			System.out.println("4 - For at afslutte");
			String choice = inFromUser.readLine().toLowerCase().trim();

			switch (choice) {
				default:
					break;
				case "1":
					System.out.println();
					System.out.println("Hvem vil du gerne forbinde til?");
					String dnsLookup = inFromUser.readLine().toLowerCase().trim();

					outToDnsServer.writeBytes(dnsLookup + " get" + '\n');

					if (!inFromDnsServer.readLine().equalsIgnoreCase(dnsLookup + " findes ikke i DNS'en")) {
						Socket connectToChatSocket = new Socket(dnsLookup, 1024);


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
					} else {
						System.out.println("DNS'en kender ikke den du forsøger at forbinde til");
					}
					loop = false;
					break;
				case "2":
					System.out.println();
					outToDnsServer.writeBytes("list" + " list" + '\n');
					String dnsRecords;
					while (!(dnsRecords = inFromDnsServer.readLine()).equals("end")) {
						System.out.println(dnsRecords);
					}
					System.out.println();
					break;
				case "3":
					System.out.println();
					System.out.println("Skriv det du gerne vil kaldes i DNS'en");
					outToDnsServer.writeBytes(inFromUser.readLine().trim() + " add" + '\n');
					System.out.println(inFromDnsServer.readLine());
					System.out.println();
					break;
				case "4":
					loop = false;
					inFromDnsServer.close();
					outToDnsServer.close();
					connectToDnsSocket.close();
					break;
			}
		}
	}
}
