package Client.UI;

import Client.ClientApp;
import Client.Networking.MainConnector;
import Client.Networking.RoomConnector;
import Client.Renderer.*;
import Templates.TScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RoomWindow extends TScene {

    RoomWindow(){
        super(new StackPane());
    }

    private Chat chat = new Chat();
    private World world = new World();
    private FpsCounter fps = new FpsCounter();
    private HealthBar healthBar = new HealthBar();
    private KillDeathRatio kdr = new KillDeathRatio();

    @Override
    public void setup() {
        StackPane root = (StackPane) getRoot();

        world.requestFocus();
        world.setFocusTraversable(true);
        root.getChildren().add(world);

        BorderPane bp = new BorderPane();
        root.getChildren().add(bp);

        VBox rightPanel = new VBox();
        rightPanel.getChildren().add(fps);
        rightPanel.getChildren().add(chat);

        bp.setRight(rightPanel);
        bp.setBottom(healthBar);

        bp.setLeft(kdr);
        Crosshair crossAir = new Crosshair();
        root.getChildren().add(crossAir);

        TopMenu top = new TopMenu();
        top.setFontColor(Color.WHITE);
        bp.setTop(top);


        setOnKeyPressed(key -> {
            switch (key.getCode()) {
                case ESCAPE:
                    top.leave();
                    break;
            }
        });
    }


    @Override
    public void refresh() {
        RoomConnector.update();

        fps.update();
        kdr.update(
                RoomConnector.getClientPlayerInfo(),
                RoomConnector.getHighestKDRPlayerInfo());
        chat.update();

        world.update(RoomConnector.getGamestate());
        healthBar.update(RoomConnector.getClientPlayerInfo().health);
    }

    @Override
    public void closingProtocol() {
        try {
            MainConnector.leaveRoom();
            MainConnector.quit();
            MainConnector.exitApplication();
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void leavingProtocol() {
        try {
            MainConnector.leaveRoom();
            ClientApp.setScene(new Lobby());
        } catch (Exception e) {e.printStackTrace();}
    }
}

