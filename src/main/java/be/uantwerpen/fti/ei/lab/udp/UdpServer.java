package be.uantwerpen.fti.ei.lab.udp;

import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    private static final int PORT = 7000;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            byte[] buffer = new byte[1024];

            System.out.println("UDP Server running...");

            while (true) {

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String filename = new String(request.getData(), 0, request.getLength());

                File file = new File(filename);

                if (!file.exists()) {
                    continue;
                }

                FileInputStream fis = new FileInputStream(file);

                byte[] sendBuffer = new byte[1024];
                int bytes;

                while ((bytes = fis.read(sendBuffer)) != -1) {

                    DatagramPacket packet =
                            new DatagramPacket(
                                    sendBuffer,
                                    bytes,
                                    request.getAddress(),
                                    request.getPort());

                    socket.send(packet);
                }

                fis.close();
                System.out.println("File sent via UDP: " + filename);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
