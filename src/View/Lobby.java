package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;


import java.util.ArrayList;

import static Fields.Constants.HEIGHT;
import static Fields.Constants.WIDTH;



public class Lobby extends Scene {

    public static final ObservableList users = FXCollections.observableArrayList();
    private final ListView listView = new ListView();
    BorderPane root;

    public Lobby() {
        super(new BorderPane(), WIDTH, HEIGHT);
        root = (BorderPane) getRoot();

        try {
            users.add("Alice");
            users.add("Bob");
            //users = Server.getUsers();

            listView.setItems(users);

            root.getChildren().add(listView);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

