package Client.UI;

import Client.ClientApp;
import Client.Networking.RoomConnector;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class Chat extends VBox {
    private TextArea messageArea;
    private String[] messages = new String[]{};
    private long lastCheck = 0;

    Chat()
    {
        this.setAlignment(Pos.CENTER);
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setFocusTraversable(false);

        this.getChildren().add(messageArea);

        TextField messageField = new TextField();
        this.getChildren().add(messageField);
        this.setMaxWidth(150);
        this.setOpacity(0.7);
        this.prefHeightProperty().bind(ClientApp.getStage().heightProperty());
        messageArea.prefHeightProperty().bind(this.heightProperty());

        messageField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                RoomConnector.sendMessage(messageField.getText());
                messageField.clear();
            }
        });
    }

    public void update() {
        if(System.currentTimeMillis() - lastCheck < 1000) return;
        lastCheck = System.currentTimeMillis();

        String[] newMessages = RoomConnector.getMessages();
        boolean refreshNeeded = newMessages.length != messages.length;

        if(!refreshNeeded)
        {
            for (int i = 0; i < messages.length; i++) {
                if (!newMessages[i].equals(messages[i])) {
                    refreshNeeded = true;
                    break;
                }
            }
        }

        if(refreshNeeded)
        {
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
    }
}
