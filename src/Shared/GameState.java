package Shared;

import java.util.ArrayList;

public class GameState {
    private ArrayList<PlayerInfo> players;

    private ArrayList<String> messages;

    public ArrayList<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerInfo> players)
    {
        this.players = players;
    }

    public ArrayList<String> getMessages () {
        for (String s : messages)
            System.out.println("Get messages: " + s);


        return messages; }

    public void addMessage(String msg)
    {
        messages.add(msg);
    }
}
