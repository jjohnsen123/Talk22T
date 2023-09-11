package V2_dns_udp_halvdelen_virker_ikke;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.HashMap;

public class DomainNameServer {
    public static void main(String[] args) throws IOException {
        DomainNameServer dns = new DomainNameServer();
        dns.records.put("localhost", "localhost");


        DatagramSocket dnsSocket = new DatagramSocket(1025);

        while (true) {
            DomainNameServerThread dnsT = new DomainNameServerThread(dns, dnsSocket);
            dnsT.start();
        }
    }
    public HashMap<String, String> records = new HashMap<>();
    public DomainNameServer() {
    }
}
