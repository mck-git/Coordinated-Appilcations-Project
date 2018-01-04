package Server;

import org.jspace.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

// Templates
// client template: ("user", string name)
// room template: ("room", string name, string owner)
// createRoom template: ("createRoom", string name, string owner)
// ack template: (string user, "response", bool true)
// nack template: (string user, "response", bool false)


public class Server {
    static Space lounge = new SequentialSpace();
    static ArrayList<Room> rooms = new ArrayList<Room>();
    static  SpaceRepository repository = new SpaceRepository();


    public static void main(String[] args)
    {


        String localhostAddress = null;
        try {
            localhostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(localhostAddress);

        repository.addGate("tcp://"+localhostAddress+":9002/?keep");
        repository.add("lounge", lounge);

        try {
            // Setup registration_lock
            lounge.put("__registration_lock");

            while (true)
            {
                handleRequests();

                displayUsers();
                displayRooms();

                Thread.sleep(1000);
            }
        } catch (Exception e) { e.printStackTrace(); }



    }

    private static void handleRequests()
    {
        try {
            // Check for createRoom requests
            List<Object[]> requests = lounge.getAll(
                    new ActualField("createRoom"),
                    new FormalField(String.class),
                    new FormalField(String.class));

            for (Object[] o : requests)
            {
              createRoom((String) o[1], (String) o[2]);
            }

        } catch (Exception e) {e.printStackTrace();}
    }


    private static void createRoom(String name, String owner) throws Exception
    {
        // room template: ("room", string name, string owner)
        // createRoom template: ("createRoom", string name, string owner)

        // Check unique
        for (Room r : rooms)
        {
            if (name.equals(r.getName()))
            {
                nack(owner);
                return;
            }
        }

        Room newRoom = new Room(name, owner);
        rooms.add(newRoom);
        repository.add(name, newRoom);

        lounge.put("room", name, owner);

        ack(owner);
    }

    private static void displayUsers()
    {
        System.out.print("Displaying users: ");
        try {
            List<Object[]> users = lounge.queryAll(new ActualField("user"), new FormalField(String.class));
            for (Object[] o : users)
            {
                System.out.print(" " + o[1]);
            }
            System.out.println();
        } catch (Exception e) {}
    }

    private static void displayRooms()
    {
        System.out.print("Displaying rooms: ");
        for (Room r : rooms)
        {
            System.out.print(" " + r.getName());
        }
        System.out.println();
    }

    private static void ack(String user) throws Exception
    {
        lounge.put(user, "response", true);
    }

    private static void nack(String user) throws Exception
    {
        lounge.put(user, "response", false);
    }



}