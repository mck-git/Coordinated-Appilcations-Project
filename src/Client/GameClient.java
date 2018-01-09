package Client;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class GameClient {

    private GameState gameState;

    public GameClient(RemoteSpace room) {
        this.gameState = new GameState();
        this.room = room;
    }

    public void updateGamestate() {
        Object[] newState = room.query(new ActualField("gamestate"), new FormalField(GameState.class));

        gameState = newState[1];
    }

    public void updateCommands(Command)







}
