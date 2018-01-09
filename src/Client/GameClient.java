package Client;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import Shared.Command;
import Shared.GameState;

public class GameClient
{

    private GameState gameState;
    private RemoteSpace room;
    private String userName;

    public GameClient(RemoteSpace room, String user)
    {
        this.gameState = new GameState();
        this.room = room;
    }

    // Update the local gamestate to be the newest gamestate in the touplespace
    public void updateGamestate()
    {
        Object[] newState = room.query(new ActualField("gamestate"), new FormalField(GameState.class));

        gameState = (GameState) newState[1];
    }

    // Update the player command in the touplespace
    public void updateCommands(Command c) throws InterruptedException
    {
        Object[] cmd = room.get(new ActualField("command"), new ActualField(userName), new FormalField(Command.class));

        room.put("command", userName, c);
    }

    public String[] displayMessages()
    {
        return gameState.messages();
    }







}
