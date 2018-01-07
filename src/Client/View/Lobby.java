package Client.View;

import Client.Client;
import Templates.TScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class Lobby extends TScene {

    Lobby(){
        super(new BorderPane());
    }

    private static final ObservableList users = FXCollections.observableArrayList();
    private static final ObservableList rooms = FXCollections.observableArrayList();
    private final ListView userListView = new ListView();
    private final ListView roomListView = new ListView();
    private final VBox roomView = new VBox();
    private final VBox userView = new VBox();

    @Override
    public void setup()
    {
        BorderPane root = (BorderPane) getRoot();

        root.setPadding(new Insets(20,20,20,30));

        Label userLable = new Label();
        userLable.setText("Users in lobby");
        userView.setAlignment(Pos.CENTER);
        userView.getChildren().add(userLable);
        userView.getChildren().add(userListView);

        Label roomLable = new Label();
        roomLable.setText("Active Rooms");
        roomView.setAlignment(Pos.CENTER);
        roomView.getChildren().add(roomLable);
        roomView.getChildren().add(roomListView);


        userListView.setItems(users);
        roomListView.setItems(rooms);

        root.setLeft(userView);
        root.setCenter(roomView);

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(10,30,10,30));
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(100);

        Button updateBtn = new Button("Update");
        Button joinRoomBtn = new Button("Join Room");
        Button createRoomBtn = new Button("Create Room");

        bottom.getChildren().add(updateBtn);
        bottom.getChildren().add(joinRoomBtn);
        bottom.getChildren().add(createRoomBtn);

        root.setBottom(bottom);

        TopMenu top = new TopMenu();
        root.setTop(top);

        userListView.setFocusTraversable(false);

        updateBtn.setOnAction( event -> {
            users.clear();
            rooms.clear();
            users.addAll(Client.getUsers());
            rooms.addAll(Client.getRooms());
        });

        joinRoomBtn.setOnAction(event -> {
            if(roomListView.getSelectionModel().getSelectedItem() != null) {
                Client.joinRoom(roomListView.getSelectionModel().getSelectedItem().toString());
                ClientDisplay.setScene(new RoomWindow());
            }
        });

        VBox popUpVBox = new VBox();
        Label createRoomlbl = new Label("Enter room name:");
        TextField createRoomtxt = new TextField();
        Button createRoomTextBtn = new Button("Create Room");


        popUpVBox.getChildren().add(createRoomlbl);
        popUpVBox.getChildren().add(createRoomtxt);
        popUpVBox.getChildren().add(createRoomTextBtn);
        popUpVBox.setStyle("-fx-background-color: LightGray; border-style: groove;");
        popUpVBox.setPadding(new Insets(10, 20, 10, 20));
        popUpVBox.setAlignment(Pos.CENTER);

        Popup popup = new Popup();
        popup.setAutoFix(false);
        popup.setHideOnEscape(true);
        popup.getContent().addAll(popUpVBox);

        createRoomBtn.setOnAction(event -> {
            if(!popup.isShowing())
                popup.show(ClientDisplay.getStage());
        });

        createRoomtxt.setOnKeyPressed(key -> {
            if(key.getCode() == KeyCode.ENTER)
            {
                createRoomTextBtn.fire();
            }
        });

        createRoomTextBtn.setOnAction(event -> {
            Client.createRoom(createRoomtxt.getText());
            createRoomtxt.clear();
            popup.hide();
            ClientDisplay.setScene(new RoomWindow());
        });

        roomListView.setOnKeyPressed(key -> {
            switch(key.getCode())
            {
                case ENTER:
                    joinRoomBtn.fire();
                    break;
                case ESCAPE:
                    top.leave();
                    break;
            }
        });

        setOnKeyPressed(key -> {
            switch(key.getCode())
            {
                case R:
                    updateBtn.fire();
                    break;
                case J:
                    joinRoomBtn.fire();
                    break;
                case C:
                    createRoomBtn.fire();
                    break;
                case Q:
                    top.leave();
                    break;
                case ESCAPE:
                    top.leave();
                    break;
            }
        });

        createRoomBtn.setOnKeyPressed(key -> {
            switch(key.getCode()) {
                case ENTER:
                    createRoomBtn.fire();
                    break;
            }
        });
        joinRoomBtn.setOnKeyPressed(key -> {
            switch(key.getCode()) {
                case ENTER:
                    joinRoomBtn.fire();
                    break;
            }
        });
        updateBtn.setOnKeyPressed(key -> {
            switch(key.getCode()) {
                case ENTER:
                    updateBtn.fire();
                    break;
            }
        });

        updateBtn.fire();
    }

    @Override
    public void refresh()
    {

    }

    @Override
    public void closingProtocol() {
        try {
            Client.quit();
            Client.exitApplication();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void leavingProtocol()
    {
        try {
            Client.quit();
            ClientDisplay.setScene(new WelcomeMenu());
        } catch (Exception ignored) {
        }
    }

}