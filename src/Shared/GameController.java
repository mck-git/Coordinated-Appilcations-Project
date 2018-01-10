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

        // Remove users that isnt in provided users array
        playerloop: for (PlayerInfo p : players)
        {
            for (String s : users)
            {
              if(p.getUsername().equals(s))
              {
                    continue playerloop;
              }
            }
            players.remove(p);
        }


        // Add users that isnt in gamestate
        userloop: for (String u : users)
        {
            for (PlayerInfo p : players)
            {
                if(u.equals(p.getUsername()))
                {
                    continue userloop;
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

        cmdloop: for (Command c : commands)
        {
            for (PlayerInfo p_inf : players)
            {
                if (p_inf.getUsername().equals(c.getUsername()))
                {
                    // Update player_info


                    continue cmdloop;
                }
            }
        }

        gs.setPlayers(players);
        return gs;
    }
}
