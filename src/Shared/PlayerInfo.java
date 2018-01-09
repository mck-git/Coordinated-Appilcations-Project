package Shared;

import javafx.geometry.Point3D;

public class PlayerInfo {
    private String username;
    private Point3D position;
    private Point3D direction;
    private int health;

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

    public String getUsername() {
        return username;
    }

    public Point3D getPosition() {
        return position;
    }

    public Point3D getDirection() {
        return direction;
    }

    public int getHealth() {
        return health;
    }
}
