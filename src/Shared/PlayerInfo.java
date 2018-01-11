package Shared;

public class PlayerInfo {
    public String username;
    public double x;
    public double y;
    public double z;
    public int angle;
    public int health;
    public boolean fire;
    public boolean dead;

    public PlayerInfo(String username)
    {
        this(username,
                100, -0.5*Constants.PLAYER_HEIGHT,100,
                0,
                100);
    }

    public PlayerInfo(double x, double z)
    {
        this("old_position",
                x, -0.5*Constants.PLAYER_HEIGHT,z,
                0,
                100);
    }

    public PlayerInfo(String username, double x, double y, double z, int angle, int health)
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
