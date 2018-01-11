package Client.UI;

import Client.ClientApp;
import Client.Networking.MainConnector;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;

public class TopMenu extends HBox {

    private Button leaveRoom;
    private Label roomlbl;
    private Label username;

    TopMenu()
    {
        this.prefWidthProperty().bind(ClientApp.getStage().widthProperty());

        Popup popup = new Popup();
        popup.setAutoFix(false);
        popup.setHideOnEscape(true);

        VBox popUpVBox = new VBox();
        HBox popupButtonRow = new HBox();
        popupButtonRow.setSpacing(20);
        popupButtonRow.setAlignment(Pos.CENTER);

        Label exitMessage = new Label("Are you sure you want to leave '" + MainConnector.getCurrentRoomName() + "'?");
        Button confirmLeaveBtn = new Button("Leave");
        confirmLeaveBtn.setTextFill(Color.RED);
        confirmLeaveBtn.setOnAction(event -> {
            try {
                popup.hide();
                ClientApp.getScene().leavingProtocol();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button cancelLeaveBtn = new Button("Stay");
        cancelLeaveBtn.setTextFill(Color.GREEN);
        cancelLeaveBtn.setOnAction(event -> popup.hide());

        popupButtonRow.getChildren().add(confirmLeaveBtn);
        popupButtonRow.getChildren().add(cancelLeaveBtn);
        exitMessage.setPadding(new Insets(20));
        popUpVBox.getChildren().add(exitMessage);
        popUpVBox.getChildren().add(popupButtonRow);

        popUpVBox.setStyle("-fx-background-color: Azure; border-style: solid;");
        popUpVBox.setPadding(new Insets(10, 20, 10, 20));
        popUpVBox.setAlignment(Pos.CENTER);

        popup.getContent().addAll(popUpVBox);

        leaveRoom = new Button("Leave Room");
        leaveRoom.setFocusTraversable(false);
        leaveRoom.setOnAction(event -> {
            popup.show(ClientApp.getStage());
            popup.requestFocus();
        });

        this.setMaxWidth(Double.MAX_VALUE);

        HBox topleft = new HBox();
        roomlbl = new Label("Room: " + MainConnector.getCurrentRoomName());
        roomlbl.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 20));
        topleft.setAlignment(Pos.CENTER_LEFT);
        topleft.getChildren().add(leaveRoom);
        topleft.prefWidthProperty().bind(this.widthProperty().multiply(0.2));


        HBox topcenter = new HBox();
        topcenter.setAlignment(Pos.CENTER);
        topcenter.getChildren().add(roomlbl);
        topcenter.prefWidthProperty().bind(this.widthProperty().multiply(0.6));

        HBox topright = new HBox();
        username = new Label("Username: " + MainConnector.getUserName());
        topright.getChildren().add(username);
        topright.prefWidthProperty().bind(this.widthProperty().multiply(0.2));

        this.getChildren().add(topleft);
        this.getChildren().add(topcenter);
        this.getChildren().add(topright);
    }

    public void leave()
    {
        leaveRoom.fire();
    }

    public void setFontColor(Color color)
    {
        this.roomlbl.setTextFill(color);
        this.username.setTextFill(color);
    }
}
