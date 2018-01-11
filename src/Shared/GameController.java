package Shared;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameController
{
    private GameState gs;
    private Map map;
    private ArrayList<Node> map_nodes;
    private ArrayList<Player> players;

    public GameController()
    {
        this.gs = new GameState();
        this.map = new Map();
        this.map_nodes = Map.getNodes();
        this.players = new ArrayList<Player>();
    }

    public GameState updateActivePlayers(String[] users)
    {
        try {
            ArrayList<PlayerInfo> player_infos = gs.getPlayer_infos();

            // Remove users that isnt in provided users array
            playerloop:
            for (int i = player_infos.size()-1; i > 0; i--) {
                for (String s : users) {
                    if (player_infos.get(i).username.equals(s)) {
                        continue playerloop;
                    }
                }

                for (int j = players.size() - 1; j > 0; j--)
                {
                    if (players.get(j).is(player_infos.get(i).username))
                    {
                        players.remove(players.get(j));
                    }
                }
                player_infos.remove(player_infos.get(i));
            }

            // Add users that isnt in gamestate and players list
            userloop:
            for (String u : users) {
                for (PlayerInfo p : player_infos) {
                    if (u.equals(p.username)) {
                        continue userloop;
                    }
                }

                PlayerInfo new_p_inf = new PlayerInfo(u);
                player_infos.add(new_p_inf);
                players.add(new Player(new_p_inf));
            }

            gs.setPlayer_infos(player_infos);

        } catch (Exception e) {e.printStackTrace();}
        return gs;
    }

    public GameState applyCommands(List<Command> commands)
    {
        ArrayList<PlayerInfo> player_infos = gs.getPlayer_infos();

        cmdloop: for (Command c : commands)
        {
            for (PlayerInfo p_inf : player_infos)
            {
                if (p_inf.username.equals(c.getUsername()))
                {
                    updatePlayerInfo(p_inf,c);

                    // KILLEM ALL
                    if (p_inf.fire)
                    {
                        checkBulletCollision(p_inf, player_infos);
                    }

                    continue cmdloop;
                }
            }
        }

        gs.setPlayer_infos(player_infos);

//        System.out.println(gs.toString());

        return gs;
    }

    private void updatePlayerInfo(PlayerInfo new_p_inf, Command c)
    {
        // Update direction
        if(c.isRotateLeft())
        {
            new_p_inf.angle = (new_p_inf.angle + Constants.PLAYER_TURN_SPEED) % 360;
        }
        if(c.isRotateRight())
        {
            new_p_inf.angle = (new_p_inf.angle - Constants.PLAYER_TURN_SPEED) % 360;
        }

        // Update position
        if (c.isForward() || c.isBackward() || c.isStrafeLeft() || c.isStrafeRight())
        {
                // possible pointer problem
               PlayerInfo old_p_inf = new PlayerInfo(new_p_inf.x, new_p_inf.z);

               double angle_radians = Math.toRadians(new_p_inf.angle);
               double xspeed = Math.sin(angle_radians) * Constants.PLAYER_SPEED;
               double zspeed = Math.cos(angle_radians) * Constants.PLAYER_SPEED;

               // Needs change since diagonal walk is faster
               // check commented code in World.
               if (c.isForward())
               {
                 new_p_inf.x += xspeed;
                 new_p_inf.z += zspeed;
               }

               if (c.isBackward())
               {
                   new_p_inf.x -= xspeed;
                   new_p_inf.z -= zspeed;
               }

               if (c.isStrafeLeft())
               {
                   new_p_inf.x -= zspeed;
                   new_p_inf.z += xspeed;
               }
               if (c.isStrafeRight())
               {
                   new_p_inf.x += zspeed;
                   new_p_inf.z -= xspeed;
               }

               // Map collision
               try
               {
                 Player p = findPlayer(new_p_inf);
                 if (p == null)
                 {
                     throw new NullPointerException();
                 }

                 // Update player with new position
                 p.update(new_p_inf);
                 ArrayList<Node> collisions = checkMapCollision(p);

                 // Sort closests nodes first

                 collisions.sort((Node a, Node b) ->
                 {
                     double da = Math.pow((old_p_inf.x - a.getTranslateX()),2)
                             + Math.pow((old_p_inf.z - a.getTranslateZ()),2);
                     double db = Math.pow((old_p_inf.x - b.getTranslateX()),2)
                             + Math.pow((old_p_inf.z - b.getTranslateZ()),2);
                     return Double.compare(da, db);
                 });
                 PlayerInfo tempnew;
                 PlayerInfo tempold = old_p_inf;
                 for (Node n : collisions)
                 {
                     tempnew = handleMapCollision(new_p_inf, tempold, n);
                     tempold = new_p_inf;
                     new_p_inf = tempnew;
                     p.update(new_p_inf);

                 }

               } catch (NullPointerException e)
               {
                   System.err.println("Player_info not found in player list!!");
               }
        }

        // Update fire
        if (c.isFire())
        {
            if (!new_p_inf.fire)
            {
                System.out.println(new_p_inf.username + " is now firing!");
            }
            new_p_inf.fire = true;
        } else
        {
            if (new_p_inf.fire)
            {
                System.out.println(new_p_inf.username + " stopped firing...");
            }
            new_p_inf.fire = false;
        }
    }

    private PlayerInfo handleMapCollision(PlayerInfo new_p_inf, PlayerInfo old_p_inf, Node collider)
    {
        double dx = Math.abs(collider.getTranslateX() - old_p_inf.x);
        double dz = Math.abs(collider.getTranslateZ() - old_p_inf.z);

        if (dz > dx)
        {
            new_p_inf.z = collider.getTranslateZ()
                    - (Math.signum(collider.getTranslateZ() - old_p_inf.z)
                    *0.5*(Constants.TILE_SIZE + Constants.PLAYER_SIZE + 0.01));
        }
        if (dx > dz)
        {
            new_p_inf.x = collider.getTranslateX()
                    - (Math.signum(collider.getTranslateX() - old_p_inf.x)
                    *0.5*(Constants.TILE_SIZE + Constants.PLAYER_SIZE + 0.01));
        }

        return new_p_inf;
    }

    private Player findPlayer(PlayerInfo p_inf)
    {
        String username = p_inf.username;
        for (Player p : players)
        {
            if (p.is(username))
            {
                return p;
            }
        }

        return null;
    }

    private ArrayList<Node> checkMapCollision(Player new_player)
    {
        ArrayList<Node> collisions = new ArrayList<>();

        for (Node n : map_nodes)
        {
            if (new_player.getBoundsInParent().intersects(n.getBoundsInParent()))
            {
                collisions.add(n);
            }
        }

        return collisions;
    }

    private void checkBulletCollision(PlayerInfo player_info, List<PlayerInfo> enemy_infos)
    {
        for (PlayerInfo enemy_inf : enemy_infos)
        {
            if (enemy_inf != player_info) {
                Line raycast = new Line();
                raycast.setRotationAxis(new Point3D(1, 0, 0));
                raycast.setRotate(90);
            }
        }
    }
}
