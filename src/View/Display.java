package View;

import Templates.TScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Display extends Application {

    private static Stage _stage;
    private static TScene scene;
    @Override
    public void start(Stage stage){

        _stage = stage;
        _stage.setScene(scene);
        scene = new WelcomeMenu();
        setScene(scene);

        _stage.setTitle("Welcome");

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                scene.refresh();
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
}
