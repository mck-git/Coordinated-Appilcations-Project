package Client;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import Shared.Command;
import Shared.GameState;

import java.util.ArrayList;
import java.util.List;

public class GameClient
{

    private static GameState gameState;
    private static RemoteSpace room;
    private static String userName = "";
    private static String message = "";

    private static boolean[] keysPressed = new boolean[8];


    // Initialize GameClient when joining a new room
    public static void initialize(RemoteSpace r, String user)
    {
        room = r;
        userName = user;

        Command c = new Command(keysPressed,userName);

        room.put("command", userName, c);

        update();
        System.out.println("Initialized the GameClient");
    }

    public static void update()
    {
        try
        {
            updateGamestate();
            updateCommand();
        } catch (Exception e) {e.printStackTrace();}
    }


    // Update the local gamestate to be the newest gamestate in the touplespace
    public static void updateGamestate() throws InterruptedException
    {
        Object[] newState = room.query(new ActualField("gamestate"), new FormalField(GameState.class));

        gameState = (GameState) newState[1];
    }

    // Update the player command in the touplespace
    public static void updateCommand() throws InterruptedException
    {
        Object[] cmd = room.getp(
                new ActualField("command"),
                new ActualField(userName),
                new FormalField(Command.class));

        Command c = new Command(keysPressed,userName);

        room.put("command", userName, c);

        // message = "";
    }


    public static void setKeyPress(String key, boolean state)
    {
        switch (key) {
            case "forward":
                keysPressed[0] = state;
                break;
            case "left":
                keysPressed[1] = state;
                break;
            case "back":
                keysPressed[2] = state;
                break;
            case "right":
                keysPressed[3] = state;
                break;
            case "rotateLeft":
                keysPressed[4] = state;
                break;
            case "rotateRight":
                keysPressed[5] = state;
                break;
            case "fire":
                keysPressed[6] = state;
        }
    }


    // Send a message to the current room
    public static void sendMessage(String msg)
    {
        room.put("message", userName, msg);
    }


    // Get all messages in the current room
    public static String[] getMessages()
    {
        try {
            // Query the messages from the server touplespace for the current room
            List<Object[]> messages = room.queryAll(
                    new ActualField("message"),
                    new FormalField(String.class),
                    new FormalField(String.class)
            );

            // Collect all messages in a string array
            String[] messages_string = new String[messages.size()];
            int i = 0;
            for (Object[] o : messages)
            {
                messages_string[i] = o[1] + ":\n" + o[2];
                i++;
            }

            // Return the string array
            return messages_string;

        } catch (Exception e) {e.printStackTrace(); return new String[] {};}
    }






}
