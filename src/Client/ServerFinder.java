package Client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;


public class ServerFinder extends Thread{
    private ArrayList<String> foundServers = null;
    private final int port = 9002;
    public void initialize(ArrayList<String> servers)
    {
        System.out.println("ServerFinder initialized");
        this.foundServers = servers;
    }

    @Override
    public void run() {
        System.out.println("ServerFinder started");
        while(true){
            try {
                update();
                System.out.println("Next round of packets");
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void update()
    {
        foundServers.clear();
        // Find the server using UDP broadcast
        try {
            //Open a random port to send the package
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);
            c.setSoTimeout(1000);

            byte[] sendData = "02148_TEAM_10_SERVER_REQUEST".getBytes();

//            System.out.println("Trying default addresses");
//            //Default two addresses
//            try{
//                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), port);
//                c.send(sendPacket);
//            } catch (Exception ignored) {}
            try{
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("192.168.0.100"), port);
                c.send(sendPacket);
            } catch (Exception ignored) {}
//            try{
//                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("0.0.0.0"), port);
//                c.send(sendPacket);
//            } catch (Exception ignored) {}

            System.out.println("Trying block 10...");
            // First private block
            for(int i2 = 0; i2 <= 255; i2++)
            for(int i3 = 0; i3 <= 255; i3++)
            for(int i4 = 0; i4 <= 255; i4++)
            {
                try{
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(String.format("10.%d.%d.%d", i2, i3, i4)), port);
                    c.send(sendPacket);
                } catch (Exception ignored) {}
            }

            System.out.println("Trying block 172...");
            // Second private block
            for(int i2 = 16; i2 <= 31; i2++)
            for(int i3 = 0; i3 <= 255; i3++)
            for(int i4 = 0; i4 <= 255; i4++)
            {
                try{
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(String.format("172.%d.%d.%d", i2, i3, i4)), port);
                    c.send(sendPacket);
                } catch (Exception ignored) {}
            }

            System.out.println("Trying block 192...");
            // Third private block
            for(int i3 = 0; i3 <= 255; i3++)
            for(int i4 = 0; i4 <= 255; i4++)
            {
                try{
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(String.format("192.168.%d.%d", i3, i4)), port);
                    c.send(sendPacket);
                } catch (Exception ignored) {}
            }


//            //Try the 255.255.255.255 first
//            try {
//                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), port);
//                c.send(sendPacket);
////                System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
//            } catch (Exception ignored) {}
//
//            // Broadcast the message over all the network interfaces
//            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
//
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
//
//                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
//                    continue; // Don't want to broadcast to the loopback interface
//                }
//
//                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
//                    InetAddress broadcast = interfaceAddress.getBroadcast();
//                    if (broadcast == null) {
//                        continue;
//                    }
//
//                    // Send the broadcast package!
//                    try {
//                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, port);
//                        c.send(sendPacket);
//                    } catch (Exception ignored) {}
//
////                    System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
//                }
//            }

//            System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

            try {
                while (true) {
                    //Wait for a response
                    byte[] recvBuf = new byte[1000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                    c.receive(receivePacket);

                    //We have a response
//                    System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

                    //Check if the message is correct
                    String message = new String(receivePacket.getData()).trim();
                    if (message.equals("02148_TEAM_10_SERVER_RESPONSE")) {
                        //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
//                        System.out.println("Found server:");
//                        System.out.println(receivePacket.getAddress());
                        if(!foundServers.contains(receivePacket.getAddress().toString()))
                            foundServers.add(receivePacket.getAddress().toString());
                    }
                }
            } catch (SocketTimeoutException ignored) {}
                //Close the port!
                c.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
