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
// quit template:       ("quit", string name, string user)

public class Server {
    private static Space lobby;
    private static SpaceRepository repository;
    private static ArrayList<Room> rooms;
    private static boolean running;

    static {
        lobby = new SequentialSpace();
        repository = new SpaceRepository();
        rooms = new ArrayList<>();
        running = true;
    }

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
        repository.add("lobby", lobby);

        try {
            // Setup registration_lock
            lobby.put("__registration_lock");

            while (running)
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
            requests = lobby.getAll(
                    new ActualField("createRoom"),
                    new FormalField(String.class),
                    new FormalField(String.class));

            for (Object[] o : requests)
            {
              createRoom((String) o[1], (String) o[2]);
            }

            // Check for joinRoom requests
            // joinRoom template: ("joinRoom", string name, string user)
            requests = lobby.getAll(
                    new ActualField("joinRoom"),
                    new FormalField(String.class),
                    new FormalField(String.class)
            );
            for (Object[] o : requests)
            {
                joinRoom((String) o[1], (String) o[2]);
            }

            // Check for quit requests
            // quit template: ("quit", string user)
            requests = lobby.getAll(
                    new ActualField("quit"),
                    new FormalField(String.class)
            );
            for (Object[] o : requests)
            {
                System.out.println("Got quit request from: " + o[1]);
                quit((String) o[1]);
            }

            // Check for leaveRoom requests
            // leaveRoom template: ("leaveRoom", string user)
            for (Room r : rooms)
            {
                requests = r.getAll(
                        new ActualField("leaveRoom"),
                        new FormalField(String.class)
                );
                for (Object[] o : requests)
                {
                        System.out.println(o[1] + " wants to leave room: " + r.getName());
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
                    System.out.println(o[2] + " wants to lock room: " + o[1]);
                    r.lock((String) o[1], (String) o[2]);
                }
            }

        } catch (Exception e) {e.printStackTrace();}
    }

    private static void leaveRoom(Room r, String user)
    {
        System.out.println("leaveRoom method run");
        Object[] result = r.getp(new ActualField(user));

        if (result == null)
        {
            nack(r.getName(), user);
            return;
        }
        ack(r.getName(), user);
    }


    private static void joinRoom(String name, String user)
    {
        System.out.println(user + " wants to join room: " + name);
        // joinRoom template: ("joinRoom", string name, string user)
        for (Room r : rooms) {
            if (r.getName().equals(name) && r.isOpen()) {
                r.put(user);

                ack("lobby", user);
                return;
            }
        }

        nack("lobby", user);
    }

    private static void quit(String user)
    {
        try {
            System.out.println(user + " wants to quit from lobby");
            // quit template: ("quit", string name, string user)
            lobby.get(new ActualField("user"), new ActualField(user));
            ack("lobby", user);
        } catch (Exception e) {
            nack("lobby", user);
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
                nack("lobby", owner);
                return;
            }
        }

        Room newRoom = new Room(name, owner);
        rooms.add(newRoom);
        repository.add(name, newRoom);
        new Thread(newRoom).start();

        lobby.put("room", name, owner);

        ack("lobby", owner);
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
            for (String value : s) {
                users_string[i] = value;
                i++;
            }
        }

       return users_string;
    }

    public static String[] getUsers()
    {
        try {
            List<Object[]> users = lobby.queryAll(new FormalField(String.class));
            String[] users_string = new String[users.size()];
            int i = 0;

            for (Object[] o : users)
            {
                users_string[i] = (String) o[0];
                i++;
            }

            return users_string;
        } catch (Exception ignored) {}

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
            if (room.equals("lobby"))
            {
                lobby.put("response", user, true);
            }
            else
            {
                for (Room r : rooms)
                {
                    if (r.getName().equals(room))
                    {
                        System.out.println("Ack to room '" + r.getName() + "' with format: " + "'response', '" + user + "', true");
                        r.put("response", user, true);
                    }
                }
            }
            System.out.println("Sending ack to " + user);
        } catch (Exception ignored) {}
    }

    private static void nack(String room, String user)
    {
        try {
            if (room.equals("lobby"))
            {
                lobby.put("response", user, false);
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
        } catch (Exception ignored) {}
    }
}