package Client.View;

import Client.Client;
import Templates.TScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import static Fields.Constants.HEIGHT;
import static Fields.Constants.WIDTH;

public class GameWindow extends TScene {

    GameWindow(){
        super(new StackPane());
    }

    private Chat chat = new Chat();

    @Override
    public void setup() {
        StackPane root = (StackPane) getRoot();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        BorderPane bp = new BorderPane();

        root.getChildren().add(canvas);
        root.getChildren().add(bp);

        bp.setRight(chat);

        TopMenu top = new TopMenu();
        bp.setTop(top);

        setOnKeyPressed(key -> {
            if(key.getCode() == KeyCode.ESCAPE){
                top.leave();
            }
        });
    }

    @Override
    public void refresh() {
        chat.updateMessageArea();
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

