package be.uantwerpen.fti.ei.lab.udp;

import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpClient {
    private static final int PORT = 7000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();

        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress address = InetAddress.getByName("localhost");

            byte[] request = filename.getBytes();

            DatagramPacket packet =
                    new DatagramPacket(request, request.length, address, PORT);

            socket.send(packet);

            byte[] buffer = new byte[65507]; // max veilige UDP payload is kleiner, maar voor kleine files ok
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);

            socket.receive(response);

            byte[] data = new byte[response.getLength()];
            System.arraycopy(response.getData(), 0, data, 0, response.getLength());

            String text = new String(data);

            if ("NOT_FOUND".equals(text)) {
                System.out.println("File not found on server.");
                return;
            }

            try (FileOutputStream fos = new FileOutputStream("udp_" + filename)) {
                fos.write(data);
            }

            System.out.println("File received via UDP.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}