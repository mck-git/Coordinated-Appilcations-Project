package Server;

import org.jspace.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

// Templates
// client template: (string name)
// room template: ("room", string name, string owner)
// ack template: ("response", string user, , bool true)
// nack template: ( "response", string user, bool false)
// message template: ("message", string user, string msg);

// Request templates
// lockRoom template: ("lockRoom", string name, string owner)
// createRoom template: ("createRoom", string name, string owner)
// joinRoom template: ("joinRoom", string name, string user)
// leaveRoom template: ("leaveRoom", string user)

public class Server {
    static Space lounge = new SequentialSpace();
    static ArrayList<Room> rooms = new ArrayList<>();
    static SpaceRepository repository = new SpaceRepository();


    public static void main(String[] args)
    {
        String localhostAddress = null;
        try {
            localhostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Host IP is: " + localhostAddress);

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

            // Check for leaveRoom requests
            // leaveRoom template: ("leaveRoom", string user)
            for (Room r : rooms)
            {
                requests = r.getAll(
                        new ActualField("leaveRoom"),
                        new FormalField(String.class),
                        new FormalField(String.class)
                );
                for (Object[] o : requests)
                {
                        System.out.println((String) o[1] + " wants to leave room " + r.getName());
                        leaveRoom(r, (String) o[1]);
                }
            }

            // Check for lockRoom requests
            // lockRoom template: ("lockRoom", string name, string owner)
            for (Room r : rooms)
            {
                requests = r.getAll(
                        new ActualField("lockRoom"),
                        new FormalField(String.class),
                        new FormalField(String.class)
                );
                for (Object[] o : requests)
                {
                    System.out.println((String) o[2] + " wants to lock room " + (String) o[1]);
                    r.lock((String) o[1], (String) o[2]);
                }
            }


        } catch (Exception e) {e.printStackTrace();}
    }

    private static void leaveRoom(Room r, String user)
    {
        Object[] result = r.getp(new ActualField(user));
        if (!(result[0] == null))
        {
            ack(r.getName(), user);
            return;
        }
        nack(r.getName(), user);
    }


    private static void joinRoom(String name, String user) {
        System.out.println(user + " wants to join room " + name);
        // joinRoom template: ("joinRoom", string name, string user)
        boolean ack = false;
        for (Room r : rooms) {
            if (r.getName().equals(name)) {
                if (r.isOpen()) {
                    r.put(user);

                    ack("lounge", user);
                    ack = true;
                    break;
                }
            }
        }

        if (!ack) {
            nack("lounge", user);
        }
    }

    private static void createRoom(String name, String owner) throws Exception
    {
        System.out.println(owner + " wants to create room " + name);
        // room template: ("room", string name, string owner)
        // createRoom template: ("createRoom", string name, string owner)

        // Check unique
        for (Room r : rooms)
        {
            if (name.equals(r.getName()))
            {
                nack("lounge", owner);
                return;
            }
        }

        Room newRoom = new Room(name, owner);
        rooms.add(newRoom);
        repository.add(name, newRoom);
        new Thread(newRoom).start();

        lounge.put("room", name, owner);

        ack("lounge", owner);
    }

    public static String[] getAllUsers()
    {
        ArrayList<String[]> users = new ArrayList<>();
        users.add(getUsers());
        for (Room r : rooms)
        {
            users.add(r.getUsers());
        }
        int size = 0;
        for (String[] s : users)
        {
            size += s.length;
        }
        String[] users_string = new String[size];
        int i = 0;
        for (String[] s : users)
        {
            for (int j = 0; j < s.length; j++)
            {
                users_string[i] = s[j];
                i++;
            }
        }

       return users_string;
    }

    public static String[] getUsers()
    {
        try {
            List<Object[]> users = lounge.queryAll(new FormalField(String.class));
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

    private static void ack(String room, String user)
    {
        try {
            if (room.equals("lounge"))
            {
                lounge.put("response", user, true);
            }
            else
            {
                for (Room r : rooms)
                {
                    if (r.getName().equals(room))
                    {
                        r.put("response", user, true);
                    }
                }
            }
            System.out.println("Sending ack to " + user);
        } catch (Exception e) {}
    }

    private static void nack(String room, String user)
    {
        try {
            if (room.equals("lounge"))
            {
                lounge.put("response", user, false);
            }
            else
            {
                for (Room r : rooms)
                {
                    if (r.getName().equals(room))
                    {
                        r.put("response", user, false);
                    }
                }
            }
            System.out.println("Sending nack to " + user);
        } catch (Exception e) {}
    }
}