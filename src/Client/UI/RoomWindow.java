package Client.UI;

import Client.ClientApp;
import Client.Networking.MainConnector;
import Client.Networking.RoomConnector;
import Client.Renderer.FpsCounter;
import Client.Renderer.KillDeathRatio;
import Client.Renderer.World;
import Templates.TScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RoomWindow extends TScene {

    RoomWindow(){
        super(new StackPane());
    }

    private Chat chat = new Chat();
    private World world = new World();
    private FpsCounter fps = new FpsCounter();
    private KillDeathRatio u_kdr = new KillDeathRatio("user");
    private KillDeathRatio top_kdr = new KillDeathRatio("top");

    @Override
    public void setup() {
        StackPane root = (StackPane) getRoot();

        world.requestFocus();
        world.setFocusTraversable(true);
        root.getChildren().add(world);

        BorderPane bp = new BorderPane();
        root.getChildren().add(bp);

        bp.setBottom(fps);
        bp.setRight(chat);

        VBox scores = new VBox();

        scores.getChildren().addAll(u_kdr,top_kdr);

        bp.setLeft(scores);

        TopMenu top = new TopMenu();
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
        u_kdr.update(RoomConnector.getPlayerInfo());
        top_kdr.update(RoomConnector.getHighestKDRPlayerInfo());
        chat.update();

        world.update(RoomConnector.getGamestate());

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

