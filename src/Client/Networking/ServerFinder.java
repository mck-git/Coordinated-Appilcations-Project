package Client.Networking;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class ServerFinder extends Thread{
    private ArrayList<String> foundServers = null;
    private final int port = 9002;
    private boolean active = false;
    private Scanner sc;

    public void initialize(ArrayList<String> servers)
    {
        System.out.println("ServerFinder initialized");
        this.foundServers = servers;
    }

    public void activate()
    {
        System.out.println("ServerFinder activating");
        this.active = true;
    }
    public void deactive()
    {
        System.out.println("ServerFinder deactivating");
        this.active = false;
    }

    @Override
    public void run() {
        System.out.println("ServerFinder started");
        while(true){
            try {
                if(active)
                    update();
                Thread.sleep(5000);
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

            sc = new Scanner(new File("src/Shared/ServerLookup.txt"));

            while(sc.hasNextLine()) {
                try {
                    String ip = sc.nextLine().trim();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
                    c.send(sendPacket);
                } catch (Exception ignored) {}
            }

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
            sc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
