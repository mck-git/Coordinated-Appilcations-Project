package View;

import Client.Client;
import Exceptions.Client.ServerNACKException;
import Fields.Constants;
import Templates.TScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class Game extends TScene {

    private BorderPane root;
    private String[] messages = new String[]{};
    private TextArea messageArea;

    @Override
    public void setup() {
        root = (BorderPane) getRoot();

        VBox chatPanel = new VBox();

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setFocusTraversable(false);

        chatPanel.getChildren().add(messageArea);

        TextField messageField = new TextField();
        chatPanel.getChildren().add(messageField);
        chatPanel.setMaxWidth(150);
        chatPanel.setOpacity(0.5);
        messageArea.setPrefHeight(Constants.HEIGHT*0.8);

        chatPanel.setSpacing(10);

        chatPanel.setOnMouseEntered(mouse -> {
            chatPanel.setOpacity(0.8);
        });

        chatPanel.setOnMouseExited(mouse -> {
            chatPanel.setOpacity(0.3);
        });
        root.setRight(chatPanel);

        messageField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                Client.sendMessage(messageField.getText());
                messageField.clear();
            }
        });

        Popup popup = new Popup();
        popup.setAutoFix(false);
        popup.setHideOnEscape(true);

        VBox popUpVBox = new VBox();
        HBox popupButtonRow = new HBox();
        popupButtonRow.setSpacing(20);
        popupButtonRow.setAlignment(Pos.CENTER);

        Label exitMessage = new Label("Are you sure you want to leave '" + Client.getCurrentRoomName());
        Button confirmLeaveBtn = new Button("Leave Room");
        confirmLeaveBtn.setOnAction(event -> {
            try {
                popup.hide();
                Client.leaveRoom();
                Display.setScene(new Lobby());
            } catch (ServerNACKException e) {
                e.printStackTrace();
            }
        });
        confirmLeaveBtn.setOnKeyPressed(key -> {
            switch (key.getCode())
            {
                case ENTER:
                    confirmLeaveBtn.fire();
                    break;
            }
        });


        Button cancelLeaveBtn = new Button("Cancel");
        cancelLeaveBtn.setOnAction(event -> {
            popup.hide();
        });
        cancelLeaveBtn.setOnKeyPressed(key -> {
            switch (key.getCode())
            {
                case ENTER:
                    cancelLeaveBtn.fire();
                    break;
            }
        });

        popupButtonRow.getChildren().add(confirmLeaveBtn);
        popupButtonRow.getChildren().add(cancelLeaveBtn);

        popUpVBox.getChildren().add(exitMessage);
        popUpVBox.getChildren().add(popupButtonRow);

        popUpVBox.setStyle("-fx-background-color: LightBlue; border-style: groove; border-width:3; border-color:black");
        popUpVBox.setPadding(new Insets(10, 20, 10, 20));
        popUpVBox.setAlignment(Pos.CENTER);

        popup.getContent().addAll(popUpVBox);

        Button exitRoom = new Button("Exit");
        exitRoom.setFocusTraversable(false);
        exitRoom.setOnAction(event -> {
            popup.show(Display.getStage());
        });

        VBox top = new VBox();
        top.setAlignment(Pos.CENTER_LEFT);
        top.getChildren().add(exitRoom);
        root.setTop(top);

        setOnKeyPressed(key -> {
            if(key.getCode() == KeyCode.ESCAPE){
                exitRoom.fire();
            }
        });
    }

    @Override
    public void refresh() {
        String[] newMessages = Client.getMessages();
        if (newMessages.length != messages.length) {
            updateMessageArea(newMessages);
            return;
        }

        for (int i = 0; i < messages.length; i++) {
            if (!newMessages[i].equals(messages[i])) {
                updateMessageArea(newMessages);
                break;
            }
        }
    }

    private void updateMessageArea(String[] newMessages) {
        messages = newMessages;
        StringBuilder chat = new StringBuilder();
        for (int i = 0; i < messages.length; i++) {
            chat.append(messages[i]);
            if(i < messages.length-1)
                chat.append("\n");
        }
        messageArea.clear();
        messageArea.appendText(chat.toString());
        messageArea.setWrapText(true);
        messageArea.setScrollTop(Double.MAX_VALUE);
    }

    @Override
    public void closingProtocol() {
        try {
            Client.leaveRoom();
            Client.quit();
            System.exit(0);
        } catch (Exception ignored) {
        }
    }
}

