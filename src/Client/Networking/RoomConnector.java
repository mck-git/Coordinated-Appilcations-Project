package Client.Networking;

import javafx.scene.input.KeyCode;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import Shared.Command;
import Shared.GameState;

import java.util.ArrayList;
import java.util.List;

public class RoomConnector
{

    private static GameState gameState;
    private static RemoteSpace room;
    private static String userName = "";
    private static boolean inRoom = false;

    private static boolean[] keysPressed = new boolean[7];


    public static void initialize(String user)
    {
        userName = user;
        inRoom = false;
        System.out.println("Initialized the RoomConnector");
    }

    // Connect to the room when joining a new room
    public static void connect(RemoteSpace r)
    {
        room = r;

        inRoom = true;

        Command c = new Command(keysPressed,userName);

        room.put("command", userName, c);

        update();
        System.out.println("Connected the RoomConnector");
    }

    public static void update()
    {
        try
        {
            if (inRoom)
            {
                updateGamestate();
                updateCommand();
            }
        } catch (Exception e) {e.printStackTrace();}
    }


    // Update the local gamestate to be the newest gamestate in the tuplespace
    public static void updateGamestate() throws InterruptedException
    {
        Object[] newState = room.queryp(new ActualField("gamestate"), new FormalField(GameState.class));

        if(newState != null)
            gameState = (GameState) newState[1];
    }

    // Update the player command in the tuplespace
    public static void updateCommand() throws InterruptedException
    {
        Object[] cmd = room.getp(
                new ActualField("command"),
                new ActualField(userName),
                new FormalField(Command.class));

        Command c = new Command(keysPressed,userName);
        room.put("command", userName, c);
    }


    public static void setKeyPress(KeyCode key, boolean state)
    {
        switch (key) {
            case W:
                keysPressed[0] = state;
                break;
            case A:
                keysPressed[1] = state;
                break;
            case S:
                keysPressed[2] = state;
                break;
            case D:
                keysPressed[3] = state;
                break;
            case LEFT:
                keysPressed[4] = state;
                break;
            case RIGHT:
                keysPressed[5] = state;
                break;
            case SPACE:
                keysPressed[6] = state;
        }
    }

    public static void leaveRoom() throws InterruptedException
    {
        // Remove command from touplespace
        room.getp(
                new ActualField("command"),
                new ActualField(userName),
                new FormalField(Command.class));

        inRoom = false;

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
                messages_string[i] = o[1] + ":" + o[2];
                i++;
            }

            // Return the string array
            return messages_string;

        } catch (Exception e) {e.printStackTrace(); return new String[] {};}
    }


    public static GameState getGamestate()
    {
        return gameState;
    }



}
