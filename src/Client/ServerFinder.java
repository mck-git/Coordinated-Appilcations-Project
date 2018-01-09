package Client;

import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class ServerFinder extends Thread{
    private ObservableList foundServers = null;
    public void initialize(ObservableList servers)
    {
        System.out.println("ServerFinder array initialized");
        this.foundServers = servers;
    }

    @Override
    public void run() {
        System.out.println("ServerFinder started");
        while(true){
            try {
                update();
                Thread.sleep(100);
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

            byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

            //Try the 255.255.255.255 first
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
                c.send(sendPacket);
                System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e) {
            }

            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                        c.send(sendPacket);
                    } catch (Exception e) {
                    }

                    System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

            try {
                while (true) {
                    //Wait for a response
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                    c.receive(receivePacket);

                    //We have a response
                    System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

                    //Check if the message is correct
                    String message = new String(receivePacket.getData()).trim();
                    if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
                        //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                        System.out.println("Found server:");
                        System.out.println(receivePacket.getAddress());
                        if(!foundServers.contains(receivePacket.getAddress().toString()))
                            foundServers.add(receivePacket.getAddress().toString());
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {}
                //Close the port!
                c.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
