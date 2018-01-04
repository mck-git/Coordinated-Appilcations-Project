package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jspace.RemoteSpace;

public class Display extends Application {

    static RemoteSpace lobby;
    static String tcp = "tcp://192.168.0.100:9002/lounge?keep";

    private static Stage _stage;
    @Override
    public void start(Stage stage) throws Exception{

        lobby = new RemoteSpace(tcp);

        _stage = stage;
        BorderPane root = new BorderPane();
        WelcomeMenu menu = new WelcomeMenu(root, lobby);
        _stage.setScene(menu);

        _stage.setTitle("Welcome");
        _stage.show();
    }

    public static void setScene(Scene scene)
    {
        _stage.setScene(scene);
    }
}
