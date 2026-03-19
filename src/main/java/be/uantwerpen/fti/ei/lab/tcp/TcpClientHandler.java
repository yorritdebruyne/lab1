package be.uantwerpen.fti.ei.lab.tcp;

import java.io.*;
import java.net.Socket;

public class TcpClientHandler implements Runnable{
    private Socket socket;
    public TcpClientHandler(Socket socket) {this.socket = socket;}

    @Override
    public void run() {
        try(
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream())
        ){
            String filename = in.readUTF();
            File file = new File (filename);

            if (!file.exists()) {
                out.writeLong(-1);
                return;
            }

            out.writeLong(file.length());

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytes;

            while ((bytes = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytes);
            }

            fis.close();
            System.out.println("File sent: " + filename);
            System.out.println("Handling client in thread: " + Thread.currentThread().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
