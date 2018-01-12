package Client.Networking;

import Shared.Command;
import Shared.GameState;
import Shared.PlayerInfo;
import javafx.scene.input.KeyCode;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.util.List;

public class RoomConnector
{

    private static GameState gameState;
    private static RemoteSpace room;
    private static String userName = "";
    private static boolean inRoom = false;
    private static PlayerInfo clientPlayerInfo;
    private static boolean[] keysPressed = new boolean[7];

    public static void initialize(String user)
    {
        userName = user;
        clientPlayerInfo = new PlayerInfo(userName);
        inRoom = false;
        System.out.println("Initialized the RoomConnector");
    }

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

    public static void updateGamestate() throws InterruptedException
    {
        Object[] newState = room.queryp(new ActualField("gamestate"), new FormalField(GameState.class));

        if(newState != null && newState.length == 2)
            gameState = (GameState) newState[1];

        clientPlayerInfo = gameState.getPlayer_info(userName);
    }

    public static void updateCommand() throws InterruptedException
    {
        room.getp(
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
        room.getp(
                new ActualField("command"),
                new ActualField(userName),
                new FormalField(Command.class));

        inRoom = false;
//        room.getGate().close();

    }

    public static void sendMessage(String msg)
    {
        room.put("message", userName, msg);
    }

    public static String[] getMessages()
    {
        try {
            List<Object[]> messages = room.queryAll(
                    new ActualField("message"),
                    new FormalField(String.class),
                    new FormalField(String.class)
            );

            String[] messages_string = new String[messages.size()];
            int i = 0;
            for (Object[] o : messages)
            {
                messages_string[i] = o[1] + ":" + o[2];
                i++;
            }

            return messages_string;
        } catch (InterruptedException e) {e.printStackTrace(); return new String[] {};}
    }


    public static GameState getGamestate()
    {
        return gameState;
    }

    public static PlayerInfo getClientPlayerInfo()
    {
        return clientPlayerInfo;
    }

    public static PlayerInfo getHighestKDRPlayerInfo()
    {
        PlayerInfo topP_I = getClientPlayerInfo();
        double topKDR = topP_I.calculateKDR();

        for( PlayerInfo pi : gameState.getPlayer_infos())
        {
            double kdr = pi.calculateKDR();
            if (kdr > topKDR)
            {
                topP_I = pi;
                topKDR = kdr;
            }
        }

        return topP_I;
    }

}
