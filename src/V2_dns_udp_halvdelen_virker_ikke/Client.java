package V2_dns_udp_halvdelen_virker_ikke;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
	public static void main(String[] args) throws Exception {
		boolean loop = true;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		int port = 1025;
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		while (loop) {
			System.out.println("Vælg tallet efter hvad du gerne vil");
			System.out.println("1 - Forbinde til en");
			System.out.println("2 - Se listen fra DNS'en");
			System.out.println("3 - Skrives op i DNS'en");
			System.out.println("4 - For at afslutte");
			String choice = inFromUser.readLine().toLowerCase().trim();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			switch (choice) {
				default:
					break;
				case "1":
					System.out.println();
					System.out.println("Hvem vil du gerne forbinde til?");
					String dnsLookup = inFromUser.readLine().toLowerCase().trim();

					String lookupToDns = (dnsLookup + " get");
					sendData = lookupToDns.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					clientSocket.send(sendPacket);
					clientSocket.receive(receivePacket);

					if (!new String(receivePacket.getData()).trim().equalsIgnoreCase(dnsLookup + " findes ikke i DNS'en")) {
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
					String listToDns = ("list" + " list" + '\n');
					sendData = listToDns.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					clientSocket.send(sendPacket);
					clientSocket.receive(receivePacket);
					String dnsRecords;
					while (!(dnsRecords = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength())).trim().equals("end")) {
						System.out.println(dnsRecords.trim());
						clientSocket.receive(receivePacket);
					}
					System.out.println();
					break;
				case "3":
					System.out.println();
					System.out.println("Skriv det du gerne vil kaldes i DNS'en");
					String nameToDns = (inFromUser.readLine().trim() + " add" + '\n');
					sendData = nameToDns.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					clientSocket.send(sendPacket);
					clientSocket.receive(receivePacket);
					System.out.println(new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength()).trim());
					System.out.println();
					break;
				case "4":
					loop = false;
					break;
			}
		}
	}
}
