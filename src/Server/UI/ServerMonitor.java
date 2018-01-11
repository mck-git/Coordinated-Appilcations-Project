package Server.UI;
import Server.ServerApp;
import Server.Networking.Server;
import Templates.TScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import static Shared.Constants.WIDTH;

public class ServerMonitor extends TScene{

    public ServerMonitor(){
        super(new BorderPane());
    }

    private static final ObservableList users = FXCollections.observableArrayList();
    private static final ObservableList rooms = FXCollections.observableArrayList();
    private final ListView userListView = new ListView();
    private final ListView roomListView = new ListView();
    private final VBox roomView = new VBox();
    private final VBox userView = new VBox();

    @Override
    public void setup() {
        BorderPane root = (BorderPane) getRoot();

        root.setPadding(new Insets(20,20,20,30));

        Label userLable = new Label();
        userLable.setText("All Users");
        userView.setAlignment(Pos.CENTER);
        userView.getChildren().add(userLable);
        userView.getChildren().add(userListView);

        Label roomLable = new Label();
        roomLable.setText("All Rooms");
        roomView.setAlignment(Pos.CENTER);
        roomView.getChildren().add(roomLable);
        roomView.getChildren().add(roomListView);

        userListView.setItems(users);
        roomListView.setItems(rooms);

        root.setLeft(userView);
        root.setCenter(roomView);

        Popup popup = new Popup();
        popup.setAutoFix(false);
        popup.setHideOnEscape(true);

        VBox popUpVBox = new VBox();
        HBox popupButtonRow = new HBox();
        popupButtonRow.setSpacing(20);
        popupButtonRow.setAlignment(Pos.CENTER);

        Label stopMessage = new Label("Are you sure you want to stop the server?");
        Button confirmStopBtn = new Button("Stop Server");
        confirmStopBtn.setOnAction(event -> {
            try {
                popup.hide();
                closingProtocol();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        confirmStopBtn.setOnKeyPressed(key -> {
            switch (key.getCode())
            {
                case ENTER:
                    confirmStopBtn.fire();
                    break;
            }
        });

        Button cancelStopBtn = new Button("Cancel");
        cancelStopBtn.setOnAction(event -> popup.hide());
        cancelStopBtn.setOnKeyPressed(key -> {
            switch (key.getCode())
            {
                case ENTER:
                    cancelStopBtn.fire();
                    break;
            }
        });

        popupButtonRow.getChildren().add(confirmStopBtn);
        popupButtonRow.getChildren().add(cancelStopBtn);

        popUpVBox.getChildren().add(stopMessage);
        popUpVBox.getChildren().add(popupButtonRow);

        popUpVBox.setStyle("-fx-background-color: LightBlue; border-style: groove; border-width:3; border-color:black");
        popUpVBox.setPadding(new Insets(10, 20, 10, 20));
        popUpVBox.setAlignment(Pos.CENTER);

        popup.getContent().addAll(popUpVBox);

        Button stopServerBtn = new Button("Stop Server");
        stopServerBtn.setFocusTraversable(false);
        stopServerBtn.setOnAction(event -> popup.show(ServerApp.getStage()));

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        top.setSpacing(WIDTH/4);
        top.getChildren().add(stopServerBtn);
        root.setTop(top);
    }

    @Override
    public void refresh() {
        users.clear();
        rooms.clear();
        users.addAll(Server.getAllUsers());
        rooms.addAll(Server.getRooms());
    }

    @Override
    public void closingProtocol() {
        System.exit(0);
    }

    @Override
    public void leavingProtocol() {
        //Nothing
    }
}
