package Shared;

import java.util.ArrayList;
import java.util.List;

public class GameController
{
    private GameState gs;


    public GameController()
    {

        this.gs = new GameState();
    }

    public GameState updatePlayerList(String[] users)
    {
        ArrayList<PlayerInfo> players = gs.getPlayers();
        for (String u : users)
        {
            for (PlayerInfo p : players)
            {
                if(users.equals(p.getUsername()))
                {
                    continue;
                }
            }

            players.add(new PlayerInfo(u));
        }

        gs.setPlayers(players);
        return gs;
    }

    public GameState applyCommands(List<Command> commands)
    {
        ArrayList<PlayerInfo> players = gs.getPlayers();

        for (Command c : commands)
        {
            for (PlayerInfo p_inf : players)
            {
                if (p_inf.getUsername().equals(c.getUsername()))
                {
                    // Update player_info

                    // Check for new message
                    String msg = c.getMessage();
                    if (!msg.equals(""))
                    {
                        gs.addMessage(p_inf.getUsername() + ": " + msg);
                    }

                    continue;
                }
            }
        }

        gs.setPlayers(players);
        return gs;
    }
}
