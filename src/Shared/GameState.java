package Shared;

import java.util.ArrayList;

public class GameState {
    private ArrayList<PlayerInfo> player_infos;

    public GameState()
    {
        this.player_infos = new ArrayList<PlayerInfo>();
    }

    public ArrayList<PlayerInfo> getPlayer_infos() {
        return player_infos;
    }

    public void setPlayer_infos(ArrayList<PlayerInfo> player_infos)
    {
        this.player_infos = player_infos;
    }

    public String toString()
    {
        String gs = "Gamestate info: \n";
        gs += "     player_infos in game: \n";
        for (PlayerInfo p : player_infos)
        {
            gs += "     " + p.toString();
        }

        return gs;
    }

}
