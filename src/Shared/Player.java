package Shared;

import javafx.scene.shape.Box;

import static Shared.Constants.PLAYER_HEIGHT;

public class Player extends Box {

    public String username;
    public boolean active = false;
    public Player(PlayerInfo info)
    {
        this.username = info.username;
        this.setHeight(PLAYER_HEIGHT);
        this.setWidth(Constants.PLAYER_SIZE);
        this.setDepth(Constants.PLAYER_SIZE);
        update(info);
    }

    public void update(PlayerInfo info)
    {
        this.setTranslateX(info.x);
        this.setTranslateY(info.y);
        this.setTranslateZ(info.z);
        this.active = true;
    }

    public boolean is(String username) {
        return this.username.equals(username);
    }
}
