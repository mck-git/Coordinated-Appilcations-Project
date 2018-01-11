package Client.UI;

import Client.ClientApp;
import Client.Networking.MainConnector;
import Client.Networking.RoomConnector;
import Client.Renderer.FpsCounter;
import Client.Renderer.HealthBar;
import Client.Renderer.KillDeathRatio;
import Client.Renderer.World;
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
    private KillDeathRatio u_kdr = new KillDeathRatio("user");
    private KillDeathRatio top_kdr = new KillDeathRatio("top");

    public long connectorTime = 0;
    public long renderTime = 0;

    @Override
    public void setup() {
        StackPane root = (StackPane) getRoot();

        world.requestFocus();
        world.setFocusTraversable(true);
        root.getChildren().add(world);

        BorderPane bp = new BorderPane();
        root.getChildren().add(bp);

//        HBox bottomPanel = new HBox();

//        bottomPanel.getChildren().add(fps);
//        bottomPanel.getChildren().add(healthBar);

        VBox rightPanel = new VBox();
        rightPanel.getChildren().add(fps);
        rightPanel.getChildren().add(chat);

        bp.setRight(rightPanel);
        bp.setBottom(healthBar);
//        bp.setRight(chat);

        VBox scores = new VBox();

        scores.getChildren().addAll(u_kdr,top_kdr);

        bp.setLeft(scores);

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
        long start = System.nanoTime();
        long temp;

        temp = System.nanoTime();
        RoomConnector.update();
        connectorTime = System.nanoTime() - temp;

        fps.update();
        u_kdr.update(RoomConnector.getClientPlayerInfo());
        top_kdr.update(RoomConnector.getHighestKDRPlayerInfo());
        chat.update();

        temp = System.nanoTime();
        world.update(RoomConnector.getGamestate());
        renderTime = System.nanoTime() - temp;

        temp = System.nanoTime();
        long time_for_everything = temp - start;

        System.out.println("connection time: " +  (100 * connectorTime / time_for_everything) + "%"
            + ". In mili seconds: " + connectorTime / 1000);
        System.out.println("render time: " + (100 * renderTime / time_for_everything) + "%"
                + ". In mili seconds: " + renderTime / 1000);

        healthBar.update(RoomConnector.getClientPlayerInfo().health);

        // world.update(RoomConnector.update());
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

