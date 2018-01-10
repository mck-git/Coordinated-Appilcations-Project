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
        try {
            ArrayList<PlayerInfo> players = gs.getPlayers();

            // Remove users that isnt in provided users array
            playerloop:
            for (PlayerInfo p : players) {
                for (String s : users) {
                    if (p.username.equals(s)) {
                        continue playerloop;
                    }
                }
                players.remove(p);
            }


            // Add users that isnt in gamestate
            userloop:
            for (String u : users) {

                for (PlayerInfo p : players) {
                    if (u.equals(p.username)) {
                        continue userloop;
                    }
                }

                players.add(new PlayerInfo(u));

            }
            gs.setPlayers(players);

        } catch (Exception ignored) {};
        return gs;
    }

    public GameState applyCommands(List<Command> commands)
    {
        ArrayList<PlayerInfo> players = gs.getPlayers();

        cmdloop: for (Command c : commands)
        {
            for (PlayerInfo p : players)
            {
                if (p.username.equals(c.getUsername()))
                {
                    updatePlayerInfo(p,c);

                    continue cmdloop;
                }
            }
        }

        gs.setPlayers(players);

        System.out.println(gs.toString());

        return gs;
    }

    private void updatePlayerInfo(PlayerInfo p, Command c)
    {
        if (c.isFire())
        {
            if (!p.fire)
            {
                System.out.println(p.username + " is now firing!");
            }
            p.fire = true;
        } else
        {
            if (p.fire)
            {
                System.out.println(p.username + " stopped firing...");
            }
            p.fire = false;
        }
    }
}
