package Client.Renderer;

import Client.ClientApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends StackPane {
    Rectangle health;
    Rectangle background;
    public HealthBar()
    {
        int sidePadding = 200;
        this.setPadding(new Insets(0,sidePadding,10,sidePadding));
        this.prefWidthProperty().bind(ClientApp.getStage().widthProperty().subtract(sidePadding * 2));
        this.setAlignment(Pos.CENTER_LEFT);

        background = new Rectangle();
        background.widthProperty().bind(this.prefWidthProperty());
        background.setHeight(20);
        background.setFill(Color.RED);
        this.getChildren().add(background);

        health = new Rectangle();
        health.setWidth(background.getWidth());
        health.setHeight(20);
        health.setFill(Color.GREEN);
        this.getChildren().add(health);
    }

    public void update(int current_health)
    {
        health.setWidth(background.getWidth()*current_health * 0.01);
    }
}
