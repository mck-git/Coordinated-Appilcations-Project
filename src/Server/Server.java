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
// joinRoom template: ("joinRoom", string name, string user)
// ack template: ("response", string user, , bool true)
// nack template: ( "response", string user, bool false)
// message template: ("message", string user, string msg);

public class Server {
    static Space lounge = new SequentialSpace();
    static ArrayList<Room> rooms = new ArrayList<Room>();
    static SpaceRepository repository = new SpaceRepository();


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


                Thread.sleep(1000);
            }
        } catch (Exception e) { e.printStackTrace(); }



    }

    private static void handleRequests()
    {
        try {
            List<Object[]> requests;

            // Check for createRoom requests
            // createRoom template: ("createRoom", string name, string owner)
            requests = lounge.getAll(
                    new ActualField("createRoom"),
                    new FormalField(String.class),
                    new FormalField(String.class));

            for (Object[] o : requests)
            {
              createRoom((String) o[1], (String) o[2]);
            }

            // Check for joinRoom requests
            // joinRoom template: ("joinRoom", string name, string user)
            requests = lounge.getAll(
                    new ActualField("joinRoom"),
                    new FormalField(String.class),
                    new FormalField(String.class)
            );
            for (Object[] o : requests)
            {
                joinRoom((String) o[1], (String) o[2]);
            }




        } catch (Exception e) {e.printStackTrace();}
    }

    private static void joinRoom(String name, String user)
    {
        // joinRoom template: ("joinRoom", string name, string user)
        boolean ack = false;
        for (Room r : rooms)
        {
            if (r.getName().equals(name))
            {
                if (r.isOpen())
                {
                    ack(user);
                    ack = true;
                    break;
                }
            }
        }

        if (!ack)
        {
            nack(user);
        }
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

    public static String[] getUsers()
    {
        try {
            List<Object[]> users = lounge.queryAll(new ActualField("user"), new FormalField(String.class));
            String[] users_string = new String[users.size()];
            int i = 0;

            for (Object[] o : users)
            {
                users_string[i] = (String) o[1];
                i++;
            }

            return users_string;
        } catch (Exception e) {}

        return null;
    }

    public static String[] getRooms()
    {
        String[] rooms_string = new String[rooms.size()];
        int i = 0;

        for (Room r : rooms)
        {
            rooms_string[i] = r.getName();
            i++;
        }
        return rooms_string;
    }

    private static void ack(String user)
    {
        try {
            lounge.put("response", user, true);
        } catch (Exception e) {}
    }

    private static void nack(String user)
    {
        try {
        lounge.put("response", user, false);
        } catch (Exception e) {}
    }



}