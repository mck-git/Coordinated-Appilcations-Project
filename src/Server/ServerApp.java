package Server;

import Server.Networking.Server;
import Server.UI.ServerMonitor;
import Templates.TScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application {


    private static Stage _stage;
    private static TScene scene;
    @Override
    public void start(Stage stage){

        _stage = stage;
        scene = new ServerMonitor();
        setScene(scene);

        _stage.setOnCloseRequest(event -> {
            scene.closingProtocol();
            event.consume();
        });
        _stage.setTitle("Server Monitor");

        Server.setup();

        new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long now) {
                if (now - lastTime > 1e9) {
                    scene.refresh();
                    lastTime = now;
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
}
