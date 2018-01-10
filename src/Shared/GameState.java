package Shared;

import java.util.ArrayList;

public class GameState {
    private ArrayList<PlayerInfo> players;

    private ArrayList<String> messages;

    public GameState()
    {
        this.players = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public ArrayList<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerInfo> players)
    {
        this.players = players;
    }

    public ArrayList<String> getMessages () {return messages;}

    public void addMessage(String msg)
    {
        messages.add(msg);
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
