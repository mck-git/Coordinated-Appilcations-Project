package Shared;

import javafx.geometry.Point3D;

public class PlayerInfo {
    public String username;
    public int x;
    public int y;
    public int z;
    public int angle;
    public int health;
    public boolean fire;

    public PlayerInfo(String username)
    {
        this(username,
                0, (int) -0.9*Constants.PLAYER_HEIGHT,0,
                0,
                100);
    }

    public PlayerInfo(String username, int x, int y, int z, int angle, int health)
    {
        this.username = username;
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
        this.health = health;
    }

    public String toString() {
        String p = username + " info: \n";
        p += "  health: " + health + "\n";
        p += "  fire: " + fire + "\n";
        p += "  position: (" + x + "," + y + "," + z  + ")\n";
        p += "  angle: " + angle + "\n";

        return p;
    }
}
