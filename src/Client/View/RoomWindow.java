package Client.View;

import Client.Client;
import Client.GameEngine.FpsCounter;
import Client.GameEngine.World;
import Templates.TScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class RoomWindow extends TScene {

    RoomWindow(){
        super(new StackPane());
    }

    private Chat chat = new Chat();
    private World world = new World();
    private FpsCounter fps = new FpsCounter();

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
        fps.update();
        chat.update();
        world.update();
    }

    @Override
    public void closingProtocol() {
        try {
            Client.leaveRoom();
            Client.quit();
            Client.exitApplication();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void leavingProtocol() {
        try {
            Client.leaveRoom();
            ClientDisplay.setScene(new Lobby());
        } catch (Exception ignored) {
        }
    }
}

