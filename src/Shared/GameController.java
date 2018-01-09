package Shared;

import java.util.List;

public class GameController
{
    private GameState gs;


    public GameController()
    {
        this.gs = new GameState();
    }

    public GameState applyCommands(List<Command> commands)
    {
        List<PlayerInfo> player_info = gs.getPlayers();

        for (Command c : commands)
        {
            for (PlayerInfo p_inf : player_info)
            {
                if (c)
            }
        }

        return gs;
    }
}
