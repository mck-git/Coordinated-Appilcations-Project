package Server.Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static Shared.Constants.BROADCAST_PORT;

public class BroadCast extends Thread{
    private DatagramSocket socket;
    @Override
    public void run() {
        try {
            socket = new DatagramSocket(BROADCAST_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (true) {
                byte[] requestBuffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(requestBuffer, requestBuffer.length);
                socket.receive(request);

//                System.out.println(getClass().getName() + "Got request received from: " + request.getAddress().getHostAddress());
//                System.out.println(getClass().getName() + "Packet data: " + new String(request.getData()).trim());

                String message = new String(request.getData()).trim();
                if (message.equals("02148_TEAM_10_SERVER_REQUEST")) {
                    byte[] sendData = "02148_TEAM_10_SERVER_RESPONSE".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, request.getAddress(), request.getPort());
                    socket.send(sendPacket);

//                    System.out.println(getClass().getName() + ">>>Sent request to: " + sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


