package Shared;

import javafx.geometry.Point3D;
import javafx.scene.shape.Line;

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

        } catch (Exception e) {e.printStackTrace();}
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

                    // KILLEM ALL
                    if (p.fire)
                    {
                        players.remove(p);
                        checkBulletCollision(p, players);
                        players.add(p);
                    }

                    continue cmdloop;
                }
            }
        }

        gs.setPlayers(players);

//        System.out.println(gs.toString());

        return gs;
    }

    private void updatePlayerInfo(PlayerInfo p, Command c)
    {
        // Update direction
        if(c.isRotateLeft())
        {
            p.angle += Constants.PLAYER_TURN_SPEED % 360;
        }
        if(c.isRotateRight())
        {
            p.angle -= Constants.PLAYER_TURN_SPEED % 360;
        }

        // Update position
        if (c.isForward() || c.isBackward() || c.isStrafeLeft() || c.isStrafeRight())
        {
               double angle_radians = Math.toRadians(p.angle);
               double xspeed = Math.sin(angle_radians) * Constants.PLAYER_SPEED;
               double zspeed = Math.cos(angle_radians) * Constants.PLAYER_SPEED;

               if (c.isForward())
               {
                 p.x += xspeed;
                 p.z += zspeed;
               }

               if (c.isBackward())
               {
                   p.x -= xspeed;
                   p.z -= zspeed;
               }

               if (c.isStrafeLeft())
               {
                   p.x -= zspeed;
                   p.z += xspeed;
               }
               if (c.isStrafeRight())
               {
                   p.x += zspeed;
                   p.z -= xspeed;
               }
        }

        // Update fire
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

    private void checkBulletCollision(PlayerInfo player, List<PlayerInfo> enemies)
    {
        for (PlayerInfo enemy : enemies)
        {
            Line raycast = new Line();
            raycast.setRotationAxis(new Point3D(1,0,0));
            raycast.setRotate(90);


        }
    }
}
