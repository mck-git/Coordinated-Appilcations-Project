package Client.View;

import Client.Client;
import Client.GameEngine.World;
import Templates.TScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class GameWindow extends TScene {

    GameWindow(){
        super(new StackPane());
    }

    private Chat chat = new Chat();
    World world = new World();

    @Override
    public void setup() {
        StackPane root = (StackPane) getRoot();

//        Group world = new Group();
//
//        SubScene subScene = new SubScene(world, WIDTH, HEIGHT);
//        root.getChildren().add(subScene);
//        subScene.setFill(Color.CADETBLUE);
//        world.requestFocus();
//        world.setFocusTraversable(true);


//        Box box = new Box(100, 100, 100);
//        world.getChildren().add(box);
//        box.setTranslateX(0);
//        box.setTranslateY(0);
//        box.setTranslateZ(10);
//
//        Box box2 = new Box(100, 100, 100);
//        world.getChildren().add(box2);
//        box2.setTranslateX(150);
//        box2.setTranslateY(0);
//        box2.setTranslateZ(10);
//
//        Box box3 = new Box(100, 100, 100);
//        world.getChildren().add(box3);
//        box3.setTranslateX(-150);
//        box3.setTranslateY(0);
//        box3.setTranslateZ(10);

        world.requestFocus();
        world.setFocusTraversable(true);
        root.getChildren().add(world);

        BorderPane bp = new BorderPane();
        root.getChildren().add(bp);
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
        chat.updateMessageArea();
        world.updateCamera();
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

