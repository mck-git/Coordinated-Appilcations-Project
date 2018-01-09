package Shared;

import java.util.ArrayList;

public class GameState {
    private ArrayList<PlayerInfo> players;

    private String[] messages;

    public ArrayList<PlayerInfo> getPlayers() {
        return players;
    }


    public String[] getMessages () {return messages;}
}
