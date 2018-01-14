package Shared;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;

import java.util.ArrayList;
import java.util.List;

import static Shared.Constants.*;

public class GameController
{
    private GameState gs;
    private Map map;
    private ArrayList<Player> players;

    public GameController()
    {
        this.gs = new GameState();
        this.map = new Map();
        this.players = new ArrayList<>();
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
                        checkBulletCollision(p_inf,player_infos, players);
                    }

                    continue cmdloop;
                }
            }
        }

        gs.setPlayer_infos(player_infos);

//        System.out.println(gs.toString());

        return gs;
    }

    public GameState updateStatus()
    {
        // Update death -> respawn
        ArrayList<PlayerInfo> playerInfos = gs.getPlayer_infos();
        for (PlayerInfo p_inf : playerInfos)
        {
            if (p_inf.dead)
            {
                Point2D respawn_pos = map.getNewRespawnLocation();

                p_inf.x = respawn_pos.getX();
                p_inf.z = respawn_pos.getY();
                p_inf.dead = false;
                p_inf.health = 100;

                findPlayer(p_inf).update(p_inf);
            }
        }

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

            double stepx = 0;
            double stepz = 0;

            // Needs change since diagonal walk is faster
            // check commented code in World.
            if (c.isForward())
            {
                stepx += xspeed;
                stepz += zspeed;
            }

            if (c.isBackward())
            {
                stepx -= xspeed;
                stepz -= zspeed;
            }

            if (c.isStrafeLeft())
            {
                stepx -= zspeed;
                stepz += xspeed;
            }
            if (c.isStrafeRight())
            {
                stepx += zspeed;
                stepz -= xspeed;
            }

            double stepMagnitude = Math.sqrt(stepx*stepx+stepz*stepz);
            if(stepMagnitude > 0) {
                new_p_inf.x += stepx / stepMagnitude;
                new_p_inf.z += stepz / stepMagnitude;
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
//                ArrayList<Node> collisions = checkMapCollision(p);
                ArrayList<Node> nearNodes = map.getSurroundingNodes((int)(new_p_inf.x/TILE_SIZE), (int) (new_p_inf.z/TILE_SIZE));

                // Sort closests nodes first

//                collisions.sort((Node a, Node b) ->
//                {
//                    double da = Math.pow((old_p_inf.x - a.getTranslateX()),2)
//                            + Math.pow((old_p_inf.z - a.getTranslateZ()),2);
//                    double db = Math.pow((old_p_inf.x - b.getTranslateX()),2)
//                            + Math.pow((old_p_inf.z - b.getTranslateZ()),2);
//                    return Double.compare(da, db);
//                });

                nearNodes.sort((Node a, Node b) ->
                {
                    double da = Math.pow((old_p_inf.x - a.getTranslateX()),2)
                            + Math.pow((old_p_inf.z - a.getTranslateZ()),2);
                    double db = Math.pow((old_p_inf.x - b.getTranslateX()),2)
                            + Math.pow((old_p_inf.z - b.getTranslateZ()),2);
                    return Double.compare(da, db);
                });

//                PlayerInfo tempnew;
//                PlayerInfo tempold = old_p_inf;
//                for (Node n : collisions)
//                {
//                    tempnew = handleMapCollision(new_p_inf, tempold, n);
//                    tempold = new_p_inf;
//                    new_p_inf = tempnew;
//                    p.update(new_p_inf);
//
//                }

                for(Node n: nearNodes)
                {
                    if(p.getBoundsInParent().intersects(n.getBoundsInParent())) {
                        new_p_inf = handleMapCollision(new_p_inf, old_p_inf, n);
                        p.update(new_p_inf);
                    }
                }

            } catch (NullPointerException e)
            {
                System.err.println("Player_info not found in player list!!");
            }
        }

        // Update fire
        new_p_inf.fireLastFrame = new_p_inf.fire;
        new_p_inf.fire = false;

        if (new_p_inf.cooldown > 0)
        {
            new_p_inf.cooldown -= 1;
        }
        if (c.isFire() && new_p_inf.cooldown <= 0)
        {
             new_p_inf.fire = true;
             new_p_inf.cooldown = Constants.FIRE_RATE;
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

//    private ArrayList<Node> checkMapCollision(Player new_player)
//    {
//        ArrayList<Node> collisions = new ArrayList<>();
//
//        for (Node n : map.getSurroundingNodes())
//        {
//            if (new_player.getBoundsInParent().intersects(n.getBoundsInParent()))
//            {
//                collisions.add(n);
//            }
//        }
//
//        return collisions;
//    }

    private void checkBulletCollision(PlayerInfo shooter, ArrayList<PlayerInfo> player_infos, ArrayList<Player> users)
    {
        double radians = Math.toRadians(shooter.angle);
        Point3D direction = new Point3D(Math.sin(radians), 0, Math.cos(radians));
        Point3D shotStart = new Point3D(shooter.x, shooter.y, shooter.z);
        double sx = shooter.x+0.5*TILE_SIZE, sz = shooter.z+0.5*TILE_SIZE;
        double range = 0;
        int steps = (map.depth()*TILE_SIZE + map.width()*TILE_SIZE)/SHOT_INTERPOLATION_INTERVAL;
        for(int i = 0; i < steps; i++)
        {
            if(sz >= 0 &&
                    sx >= 0 &&
                    sz < map.depth()*TILE_SIZE &&
                    sx < map.width()*TILE_SIZE &&
                    map.grid[(int) (sz/TILE_SIZE)][(int)(sx/TILE_SIZE)] == 1) {
                break;
            }

            sx += SHOT_INTERPOLATION_INTERVAL *direction.getX();
            sz += SHOT_INTERPOLATION_INTERVAL *direction.getZ();
            range += SHOT_INTERPOLATION_INTERVAL;
        }

        Point3D shotEnd = new Point3D(shooter.x + direction.getX(), shooter.y, shooter.z + direction.getZ());

        for(Player enemy : users)
        {
            Point3D enemyPosition = new Point3D(enemy.getTranslateX(), enemy.getTranslateY(), enemy.getTranslateZ());
            double player_offset = (enemyPosition.subtract(shotStart).crossProduct(enemyPosition.subtract(shotEnd))).magnitude()/
                                            (shotEnd.subtract(shotStart)).magnitude();

            double distance = enemyPosition.subtract(shotStart).magnitude();
            double anglediff = enemyPosition.subtract(shotStart).angle(shotEnd.subtract(shotStart));
            anglediff = Double.isNaN(anglediff) ? 180 : anglediff;

            if(anglediff < 90 && distance <= range && player_offset < SHOT_RADIUS + 0.5*PLAYER_SIZE)
            {
                for(PlayerInfo enemy_inf : player_infos)
                {
                    if(!shooter.equals(enemy_inf) && enemy_inf.username.equals(enemy.username))
                    {
                        enemy_inf.health -= SHOT_DAMAGE;
                        if(enemy_inf.health <= 0) {
                            enemy_inf.dead = true;
                            enemy_inf.deaths += 1;
                            shooter.kills += 1;
                            System.out.println(shooter.username + " killed " + enemy_inf.username);
                        }
                    }
                }
            }
        }
    }
}
