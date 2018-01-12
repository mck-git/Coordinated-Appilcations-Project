package Client.Networking;

import Shared.Exceptions.CommandException;
import Shared.Exceptions.ServerNAckException;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Shared.Constants.GAME_PORT;

public class MainConnector {
    static private RemoteSpace lobby;
    static private RemoteSpace currentRoom;

    static private String userName = "";
    static private String currentRoomName = "";
    static private String ip = "";

    private static ServerFinder sf = new ServerFinder();
    public static ArrayList<String> foundServers = new ArrayList<>();

    public static void setupServerFinder()
    {
        sf.initialize(foundServers);
        sf.start();
    }

    public static void activateServerFinder()
    {
        sf.activate();
    }
    public static void deactivateServerFinder()
    {
        sf.deactivate();
    }

    public static boolean initialize(String nameInput, String serverAddress)
    {
        try
        {
            ip = serverAddress.replace("/", "");
            String tcp = createURI("lobby");

            // Connect to the lobby
            lobby = new RemoteSpace(tcp);
            currentRoom = lobby;
            currentRoomName = "lobby";

            System.out.println("Connected to server!");
            boolean taken = false;

            // Take the lock - Assures no two users try to register at once
            lobby.get(new ActualField("__registration_lock"));

            // Read all existing users in the lounge touplespace
            List<Object[]> userList = lobby.queryAll(
                    new ActualField("user"),
                    new FormalField(String.class));

            // Go through found users - if one matches: set taken to true
            for (Object[] u : userList)
            {
                String user = (String) u[1];
                if (user.equals(nameInput)) {
                    taken = true;
                    break;
                }
            }

            if (!taken)
            {
                lobby.put("user", nameInput);
                userName = nameInput;
                MainConnector.deactivateServerFinder();
                RoomConnector.initialize(nameInput);
            }

            // Release lock
            lobby.put("__registration_lock");

            return !taken;
        } catch (Exception e) {e.printStackTrace(); return false;}
    }

    // Create a room from the lobby
    public static void createRoom(String name) throws ServerNAckException {
        try
        {
            lobby.put("createRoom", name, userName);

            // Get the response
            Object[] response = lobby.get(
                    new ActualField("response"),
                    new ActualField(userName),
                    new FormalField(Boolean.class));

            // If the response was an ACK - join room
            if ((boolean) response[2])
            {
                joinRoom(name);
            }
            else
            {
                throw new ServerNAckException("createRoom");
            }
        } catch (InterruptedException e) {System.out.println("Request failed, please try again");}
    }

    // Join a room with a specific room name
    public static void joinRoom(String roomName) throws ServerNAckException {
        try
        {
            if (!currentRoomName.equals("lobby"))
                leaveRoom();

            System.out.println("Joining room " + roomName);
            // Send a message to the server that the user wishes to join the specific room
            lobby.put("joinRoom",roomName,userName);

            // Get response from server
            Object[] response = lobby.get(
                    new ActualField("response"),
                    new ActualField(userName),
                    new FormalField(Boolean.class));

            // If NACK was recieved, throw error
            if (!(boolean) response[2])
            {
                throw new ServerNAckException("joinRoom");
            }
            // Join the room by setting currentRoom to the specified room
            currentRoom = new RemoteSpace(createURI(roomName));

            // Put the username into the joined room, and set new room as currentRoomName
            currentRoomName = roomName;
            RoomConnector.connect(currentRoom);

        } catch (InterruptedException | IOException e)
        {
            e.printStackTrace();
        }
    }

    // Leave the current room, if it is not the lobby
    public static void leaveRoom() throws ServerNAckException {
        try {

            // If you are in a room other than the lobby
            if (!currentRoomName.equals("lobby"))
            {
                System.out.println("Leaving room: " + currentRoomName);
                currentRoom.put("leaveRoom", userName);
                Object[] response = currentRoom.get(
                        new ActualField("response"),
                        new ActualField(userName),
                        new FormalField(Boolean.class));
                System.out.println("Got server response: " + response[2]);

                if (!(boolean) response[2]) {
                    System.out.println("Got nack");
                    throw new ServerNAckException("leaveRoom");
                }

                // Remove yourself from the room and set currentRoom to be the lounge
                RoomConnector.leaveRoom();
                currentRoom = lobby;
                currentRoomName = "lobby";
            }
        } catch (InterruptedException e) {e.printStackTrace();}
    }

    public static void quit() throws ServerNAckException, CommandException {
        if(!currentRoomName.equals("lobby")) throw new CommandException("Illegal quit from "+currentRoomName+". Only from lobby");
        try {
            lobby.put("quit", userName);

            Object[] response = lobby.get(
                    new ActualField("response"),
                    new ActualField(userName),
                    new FormalField(Boolean.class));
            if (!(boolean)response[2])
                throw new ServerNAckException("quit");

            MainConnector.activateServerFinder();
        } catch (InterruptedException e) {e.printStackTrace();}
    }

    // Lock the current room
    public static void lockRoom()
    {
        currentRoom.put("lockRoom",currentRoomName,userName);
    }

    public static String[] getUsers()
    {
        try {
            List<Object[]> users = currentRoom.queryAll(
                    new ActualField("user"),
                    new FormalField(String.class));

            String[] users_string = new String[users.size()];
            int i = 0;

            for (Object[] o : users)
            {
                String username = (String) o[1];
                users_string[i] = username;
                i++;
            }

            return users_string;
        } catch (Exception e) {e.printStackTrace();}

        return null;
    }

    public static String[] getRooms()
    {
        try {
            List<Object[]> rooms = lobby.queryAll(
                    new ActualField("room"),
                    new FormalField(String.class),
                    new FormalField(String.class));

            String[] rooms_string = new String[rooms.size()];
            int i = 0;

            for (Object[] o : rooms)
            {
                rooms_string[i] = (String) o[1];
                i++;
            }

            return rooms_string;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    public static String getCurrentRoomName()
    {
        return currentRoomName;
    }

    public static String getUserName()
    {
        return userName;
    }



    public static void exitApplication()
    {
//        lobby.getGate().close();
        System.exit(0);
    }

    // Creates a URI address from a given room name
    public static String createURI(String roomName)
    {
        return "tcp://" + ip + ":" + GAME_PORT + "/" + roomName + "?keep";
    }
}
