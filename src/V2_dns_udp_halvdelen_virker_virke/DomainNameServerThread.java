package V2_dns_udp_halvdelen_virker_virke;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DomainNameServerThread extends Thread {
    private DomainNameServer dns;
    private DatagramSocket dnsSocket;
    private String job;

    public DomainNameServerThread(DomainNameServer dns, DatagramSocket dnsSocket) {
        this.dns = dns;
        this.dnsSocket = dnsSocket;
    }

    public void run() {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            dnsSocket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] sendData = new byte[1024];
        while (true) {
            try {
                String[] input = new String(receivePacket.getData()).split(" ");
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                job = input[1].trim();
                if (job.equals("get")) {
                    if (dns.records.containsKey(input[0].trim())) {
                        String outRecord = dns.records.get(input[0].trim());
                        sendData = outRecord.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        dnsSocket.send(sendPacket);

                    } else {
                        String noRecord = (input[0].trim() + " findes ikke i DNS'en");
                        sendData = noRecord.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        dnsSocket.send(sendPacket);
                    }
                } else if (job.equals("list")) {
                    dns.records.forEach((key, value) -> {
                        try {
                            String dnsKeyValue = ("Navn: " + key + " adresse: " + value);
                            byte[] sendDataLambda = dnsKeyValue.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendDataLambda, sendDataLambda.length, IPAddress, port);
                            dnsSocket.send(sendPacket);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    String end = ("end");
                    sendData = end.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    dnsSocket.send(sendPacket);
                } else if (job.equals("add")) {
                    System.out.println("Hit");
                    dns.records.put(input[0].trim(), String.valueOf(IPAddress).substring(1));
                    String added = ("Tilføjet");
                    sendData = added.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    dnsSocket.send(sendPacket);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
