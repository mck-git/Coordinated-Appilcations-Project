package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Display extends Application {

    private static Stage _stage;
    @Override
    public void start(Stage stage) {
        _stage = stage;
        BorderPane root = new BorderPane();
//        Scene scene = new Scene(root, 600, 400);
        WelcomeMenu menu = new WelcomeMenu(root);
        _stage.setScene(menu);

        _stage.setTitle("Welcome");
        _stage.show();
    }

    public static void setScene(Scene scene)
    {
        _stage.setScene(scene);
    }
}
