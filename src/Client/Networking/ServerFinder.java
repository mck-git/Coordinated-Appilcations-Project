package Client.Networking;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import static Shared.Constants.BROADCAST_PORT;

public class ServerFinder extends Thread{
    private ArrayList<String> foundServers = null;
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

    public void deactivate()
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
        try {
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);
            c.setSoTimeout(1000);

            byte[] sendData = "02148_TEAM_10_SERVER_REQUEST".getBytes();

            sc = new Scanner(new File("src/Shared/ServerLookup.txt"));

            while(sc.hasNextLine()) {
                try {
                    String ip = sc.nextLine().trim();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), BROADCAST_PORT);
                    c.send(sendPacket);
                } catch (Exception ignored) {}
            }

            try {
                while (true) {
                    byte[] responseBuffer = new byte[1000];
                    DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                    c.receive(responsePacket);

//                    System.out.println(getClass().getName() + ">>> Broadcast response from server: " + responsePacket.getAddress().getHostAddress());

                    String message = new String(responsePacket.getData()).trim();
                    if (message.equals("02148_TEAM_10_SERVER_RESPONSE")) {
                        if(!foundServers.contains(responsePacket.getAddress().toString()))
                            foundServers.add(responsePacket.getAddress().toString());
                    }
                }
            } catch (SocketTimeoutException ignored) {}

            c.close();
            sc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
