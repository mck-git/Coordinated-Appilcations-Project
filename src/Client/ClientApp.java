package Client;

import Client.Networking.MainConnector;
import Client.Networking.RoomConnector;
import Client.UI.WelcomeMenu;
import Templates.TScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApp extends Application {

    private static Stage _stage;
    private static TScene scene;
    @Override
    public void start(Stage stage){

        _stage = stage;
        scene = new WelcomeMenu();
        setScene(scene);
        _stage.setOnCloseRequest(event -> {
            scene.closingProtocol();
            event.consume();
        });
        _stage.setTitle("Game MainConnector");

        MainConnector.setupServerFinder();
        MainConnector.activateServerFinder();

        new AnimationTimer() {
//            long lastTime = 0;
            @Override
            public void handle(long now) {
//                if(now-lastTime > 1e9)
                {
//                    lastTime = now;
                    RoomConnector.update();
                    scene.refresh();
                }
            }
        }.start();

        _stage.show();
    }

    public static void setScene(TScene scene_)
    {
        scene = scene_;
        _stage.setScene(scene);
        scene.setup();
    }

    public static Stage getStage()
    {
        return _stage;
    }


    public static TScene getScene() {
        return scene;
    }
}
