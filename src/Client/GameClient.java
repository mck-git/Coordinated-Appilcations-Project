package Client;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import Shared.Command;
import Shared.GameState;

public class GameClient
{

    private static GameState gameState;
    private static RemoteSpace room;
    private static String userName;
    private static String message;

    // Initialize GameClient when joining a new room
    public static void initialize(RemoteSpace r, String user)
    {
        room = r;
        userName = user;
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
        Object[] cmd = room.get(new ActualField("command"), new ActualField(userName), new FormalField(Command.class));

        boolean[] bools = new boolean[7];

        Command c = new Command(bools,message);

        room.put("command", userName, c);
    }

    public static String[] getMessages()
    {
        return gameState.getMessages();
    }

    public static void sendMessage(String msg)
    {
        message = msg;
    }






}
