package be.uantwerpen.fti.ei.lab.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpFileClient {
    private static final String SERVER = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();

        try (
                Socket socket = new Socket(SERVER, PORT);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream())
        ) {

            out.writeUTF(filename);

            long fileSize = in.readLong();

            if (fileSize == -1) {
                System.out.println("File not found on server.");
                return;
            }

            FileOutputStream fos = new FileOutputStream("received_" + filename);

            byte[] buffer = new byte[4096];
            int bytes;

            while (fileSize > 0 &&
                    (bytes = in.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {

                fos.write(buffer, 0, bytes);
                fileSize -= bytes;
            }

            fos.close();

            System.out.println("File received.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
