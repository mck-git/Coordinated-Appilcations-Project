package Client.Networking;

import Shared.Exceptions.CommandException;
import Shared.Exceptions.ServerNACKException;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.util.List;
import java.util.Scanner;

public class MainConnector {
    static private RemoteSpace lobby;
    static private RemoteSpace currentRoom;

    static private String userName = "";
    static private String currentRoomName = "";
    static private String ip = "192.168.0.100";

    static private Scanner sc;

    public static void main(String[] args)
    {
        try {
            sc = new Scanner(System.in);
            sc.useDelimiter("\\n");

            System.out.println("Welcome! Please wait while connecting to the server.");
            initialize(sc.next());

            label:
            while (true) {

                System.out.println("\n\nWhat do you want to do?");
                String input = sc.next();
                input = input + sc.nextLine();
                input = input.toLowerCase();

                switch (input) {
                    case "create room":
                        System.out.print("Choose a name for your room: ");
                        String name = sc.next();
                        createRoom(name);
                        break;
                    case "join room":
                        System.out.print("Which room do you want to join? ");
                        String roomName = sc.next();
                        joinRoom(roomName);
                        break;
                    case "leave room":
                        System.out.println("Returning to the lobby...");
                        leaveRoom();
                        break;
                    case "lock room":
                        System.out.println("Locking " + currentRoomName + "...");
                        lockRoom();
                        break;
                    case "send message":
                        System.out.println("What do you wish to send?");
                        String message = sc.next();
                        message = message + sc.nextLine();
                        RoomConnector.sendMessage(message);
                        break;
                    case "get messages":
                        System.out.println("Getting messages...");
                        String[] messages = RoomConnector.getMessages();
                        for (String m : messages) {
                            System.out.println(m);
                        }
                        break;
                    case "exit":
                        System.out.println("We hope you enjoyed your stay. Goodbye");
                        break label;
                    default:
                        System.out.println("Sorry, I did not understand that.");
                        break;
                }

            }
        } catch(Exception e) {e.printStackTrace();}
        System.exit(0);
    }


    // Initialize user when first starting app
    public static boolean initialize(String nameInput)
    {
        try
        {
            String tcp = createURI("lobby");
            // Connect to the lobby
            lobby = new RemoteSpace(tcp);
            currentRoom = new RemoteSpace(tcp);
            currentRoomName = "lobby";
            System.out.println("Connected to server!");
            boolean taken = false;


            System.out.print("Please choose your desired username: ");

            // Wait for user to input desired username

            System.out.println("Your username is: " + nameInput);

            // Take the lock - Assures no two users try to register at once
            lobby.get(new ActualField("__registration_lock"));

            // Read all existing users in the lounge touplespace
            List<Object[]> userList = lobby.queryAll(
                    new ActualField("user"),
                    new FormalField(String.class));

            System.out.println("Checking if your username is taken...");


            // Go through found users - if one matches: set taken to true
            for (Object[] u : userList) {
                String user = (String) u[1];

                // If found username matches with desired username: break and set taken to true
                if (user.equals(nameInput)) {
                    System.out.println("That username was taken :(");
                    taken = true;
                    break;
                }
            }

            // If username is taken: return
            if (taken) {
                System.out.println("Please try again another time :)");
            }

            // If username is not taken: add username to lounge touplespace
            else {

                System.out.println("You chose a unique username! Good job (y)");
                lobby.put("user", nameInput);
                userName = nameInput;
                RoomConnector.initialize(nameInput);
            }

            // Release lock
            lobby.put("__registration_lock");

            return !taken;



        } catch (Exception e) {e.printStackTrace(); return false;}
    }

    // Create a room from the lobby
    public static void createRoom(String name)
    {
        // Put the request onto the server touplespace
        lobby.put("createRoom",name, userName);

        try
        {
            System.out.println("Trying to create room...");
            // Get the response
            Object[] response = lobby.get(
                    new ActualField("response"),
                    new ActualField(userName),
                    new FormalField(Boolean.class));

            // If the response was an ACK - join room
            if ((boolean) response[2])
            {
                System.out.println("Room created!");
                joinRoom(name);
            }

            // If the response was a NACK - throw error
            else
            {
                throw new ServerNACKException("createRoom");
            }
        } catch (Exception e) {System.out.println("Request failed, please try again");}
    }

    // Join a room with a specific room name
    public static void joinRoom(String roomName)
    {
        try
        {
            if (!currentRoomName.equals("lobby"))
                leaveRoom();

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
                throw new ServerNACKException("joinRoom");
            }
            // Join the room by setting currentRoom to the specified room
            currentRoom = new RemoteSpace(createURI(roomName));

            // Put the username into the joined room, and set new room as currentRoomName
            currentRoomName = roomName;
            RoomConnector.connect(currentRoom);
            System.out.println("Joined the room! Welcome to " + roomName + "!");


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Leave the current room, if it is not the lobby
    public static void leaveRoom() throws ServerNACKException {
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
                    throw new ServerNACKException("leaveRoom");
                }


                // Remove yourself from the room and set currentRoom to be the lounge
                RoomConnector.leaveRoom();
                currentRoom = lobby;
                currentRoomName = "lobby";
            }
        } catch (InterruptedException e) {e.printStackTrace();}
    }

    // Leave the current room, if it is not the lobby
    public static void quit() throws ServerNACKException, CommandException {
        if(!currentRoomName.equals("lobby")) throw new CommandException("Illegal quit from "+ MainConnector.getCurrentRoomName()+". Only from lobby");
        try {
            lobby.put("quit", userName);

            Object[] response = lobby.get(
                    new ActualField("response"),
                    new ActualField(userName),
                    new FormalField(Boolean.class));
            if (!(boolean)response[2])
                throw new ServerNACKException("quit");
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
        System.exit(0);
    }

    // Creates a URI address from a given room name
    public static String createURI(String roomName)
    {
        return "tcp://" + ip + ":9002/" + roomName + "?keep";
    }


    // IF WE WANT CHAT IN LOBBY - UNCOMMENT AND REMOVE THE METHODS FROM GAMECLIENT

    //    // Send a message to the current room
//    public static void sendMessage(String msg)
//    {
//        currentRoom.put("message", userName, msg);
//    }
//
//
//    // Get all messages in the current room
//    public static String[] getMessages()
//    {
//        try {
//            // Query the messages from the server touplespace for the current room
//            List<Object[]> messages = currentRoom.queryAll(
//                    new ActualField("message"),
//                    new FormalField(String.class),
//                    new FormalField(String.class)
//            );
//
//            // Collect all messages in a string array
//            String[] messages_string = new String[messages.size()];
//            int i = 0;
//            for (Object[] o : messages)
//            {
//                messages_string[i] = o[1] + ":\n" + o[2];
//                i++;
//            }
//
//            // Return the string array
//            return messages_string;
//
//        } catch (Exception e) {e.printStackTrace(); return new String[] {};}
//    }

}
