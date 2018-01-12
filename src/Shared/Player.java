package Shared;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
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
        this.setMaterial(new PhongMaterial(Color.color(1 - info.health / 100.0, info.health / 100.0, 0 )));
        ((PhongMaterial)this.getMaterial()).setSelfIlluminationMap(new Image("Shared/Resources/slim_creeper_gray.png"));
        update(info);
    }

    public void update(PlayerInfo info)
    {
        this.setTranslateX(info.x);
        this.setTranslateY(info.y);
        this.setTranslateZ(info.z);
        this.active = true;
        double ratio = info.health < 0 ? 0 : info.health / 100.0;
        ((PhongMaterial)this.getMaterial()).setDiffuseColor(Color.color(1 - ratio, ratio, 0 ));
    }

    public boolean is(String username) {
        return this.username.equals(username);
    }
}
