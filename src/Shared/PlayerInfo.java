package Shared;

import javafx.geometry.Point3D;

public class PlayerInfo {
    public String username;
    public Point3D position;
    public Point3D direction;
    public int health;
    public boolean fire;

    public PlayerInfo(String username)
    {
        this.username = username;
    }

    public PlayerInfo(String username, Point3D pos, Point3D dir, int health)
    {
        this.username = username;
        this.position = pos;
        this.direction = dir;
        this.health = health;
    }

    public String toString() {
        String p = username + " info: \n";
        p += "  health: " + health + "\n";
        p += "  fire: " + fire + "\n";
        p += "  position: " + position.toString() + "\n";
        p += "  direction: " + direction.toString() + "\n";

        return p;
    }
}
