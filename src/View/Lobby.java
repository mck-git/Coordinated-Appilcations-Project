package View;

import Client.Client;
import Templates.TScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Lobby extends TScene {
    private BorderPane root;
    public static final ObservableList users = FXCollections.observableArrayList();
    public static final ObservableList rooms = FXCollections.observableArrayList();
    private final ListView userListView = new ListView();
    private final ListView roomListView = new ListView();
    private final VBox roomView = new VBox();
    private final VBox userView = new VBox();
    public Lobby() {
        root = (BorderPane) getRoot();
    }

    @Override
    public void setup()
    {
        System.out.println("Settting up lobby");

        root.setPadding(new Insets(20,20,20,30));

        Label userLable = new Label();
        userLable.setText("Users");
        userView.setAlignment(Pos.CENTER);
        userView.getChildren().add(userLable);
        userView.getChildren().add(userListView);

        Label roomLable = new Label();
        roomLable.setText("Rooms");
        roomView.setAlignment(Pos.CENTER);
        roomView.getChildren().add(roomLable);
        roomView.getChildren().add(roomListView);

        userListView.setItems(users);
        roomListView.setItems(rooms);

        root.setLeft(userView);
        root.setCenter(roomView);
        users.addAll(Client.getUsers());
        rooms.addAll(Client.getRooms());

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(10,30,10,30));
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(100);

        Button updateBtn = new Button("Update");
        Button goToBtn = new Button("Join room");
        bottom.getChildren().add(updateBtn);
        bottom.getChildren().add(goToBtn);

        root.setBottom(bottom);

        updateBtn.setOnAction( event -> {
            users.clear();
            rooms.clear();
            users.addAll(Client.getUsers());
            rooms.addAll(Client.getRooms());
        });

        goToBtn.setOnAction(event -> {
            Client.joinRoom(roomListView.getSelectionModel().getSelectedItem().toString());
        });

        setOnKeyPressed(key -> {
            if(key.getCode() == KeyCode.R){
                updateBtn.fire();
            }
        });


    }

    @Override
    public void refresh()
    {

    }
}