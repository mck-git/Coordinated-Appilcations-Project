package Shared;

import java.util.ArrayList;

public class GameState {
    private ArrayList<PlayerInfo> players;

    public GameState()
    {
        this.players = new ArrayList<>();
    }

    public ArrayList<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerInfo> players)
    {
        this.players = players;
    }

    public String toString()
    {
        String gs = "Gamestate info: \n";
        gs += " players in game: \n";
        for (PlayerInfo p : players)
        {
            gs += p.toString();
        }

        return gs;
    }

}
