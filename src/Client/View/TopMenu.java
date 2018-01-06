package Client.View;

import Client.Client;
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

import static Fields.Constants.WIDTH;

public class TopMenu extends HBox {

    private Button leaveRoom;

    TopMenu()
    {
        Popup popup = new Popup();
        popup.setAutoFix(false);
        popup.setHideOnEscape(true);

        VBox popUpVBox = new VBox();
        HBox popupButtonRow = new HBox();
        popupButtonRow.setSpacing(20);
        popupButtonRow.setAlignment(Pos.CENTER);

        Label exitMessage = new Label("Are you sure you want to leave '" + Client.getCurrentRoomName() + "'?");
        Button confirmLeaveBtn = new Button("Leave");
        confirmLeaveBtn.setTextFill(Color.RED);
        confirmLeaveBtn.setOnAction(event -> {
            try {
                popup.hide();
                ClientDisplay.getScene().leavingProtocol();
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
            popup.show(ClientDisplay.getStage());
            popup.requestFocus();
        });

        this.setMaxWidth(Double.MAX_VALUE);

        HBox topleft = new HBox();
        Label roomlbl = new Label("Room: " + Client.getCurrentRoomName());
        roomlbl.setFont(Font.font("Courier", FontWeight.EXTRA_BOLD, 20));
        topleft.setAlignment(Pos.CENTER_LEFT);
        topleft.getChildren().add(leaveRoom);
        topleft.setMaxWidth(Double.MAX_VALUE);
        topleft.setPrefWidth(0.2*WIDTH);

        HBox topcenter = new HBox();
        topcenter.setAlignment(Pos.CENTER);
        topcenter.getChildren().add(roomlbl);
        topcenter.setMaxWidth(Double.MAX_VALUE);
        topcenter.setPrefWidth(0.6*WIDTH);

        HBox topright = new HBox();
        Label username = new Label("Username: " + Client.getUserName());
        topright.getChildren().add(username);
        topright.setMaxWidth(Double.MAX_VALUE);
        topright.setPrefWidth(0.2*WIDTH);

        this.getChildren().add(topleft);
        this.getChildren().add(topcenter);
        this.getChildren().add(topright);
    }

    public void leave()
    {
        leaveRoom.fire();
    }
}
