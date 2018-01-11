package Shared;

public class PlayerInfo {
    public String username;
    public double x;
    public double y;
    public double z;
    public int angle;
    public int health;
    public boolean fire;

    public int kills;
    public int deaths;

    public PlayerInfo(String username)
    {
        this(username,
                0, -0.5*Constants.PLAYER_HEIGHT,0,
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

    public double calculateKDR()
    {
        if(deaths != 0)
        {
            double kdr = ((double) kills) / ((double) deaths);
            return Math.round(kdr*10) / 10;
        }
        else
            return kills;
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
