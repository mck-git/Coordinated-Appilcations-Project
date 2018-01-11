package Server.Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadCast extends Thread{
    private DatagramSocket socket;
    private int port = 9002;
    @Override
    public void run() {
        try {
            socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));
//            socket = new DatagramSocket(port);
            socket.setBroadcast(true);

            while (true) {
//                System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

                //Receive a packet
                byte[] recvBuf = new byte[1000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //Packet received
                System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
                System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()).trim());

                //Check packet
                String message = new String(packet.getData()).trim();
                if (message.equals("02148_TEAM_10_SERVER_REQUEST")) {
                    byte[] sendData = "02148_TEAM_10_SERVER_RESPONSE".getBytes();

                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

//                    System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


