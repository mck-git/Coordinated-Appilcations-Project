package Client.View;

import Client.Client;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class Chat extends VBox {
    private TextArea messageArea;
    private String[] messages = new String[]{};

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
        this.setOpacity(0.2);
        this.prefHeightProperty().bind(ClientDisplay.getStage().heightProperty());
        messageArea.prefHeightProperty().bind(this.heightProperty().multiply(0.8));

        this.setSpacing(10);

        this.setOnMouseEntered(mouse -> this.setOpacity(0.8));
        this.setOnMouseExited(mouse -> this.setOpacity(0.2));

        messageField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                Client.sendMessage(messageField.getText());
                messageField.clear();
            }
        });
    }

    public void update() {
        String[] newMessages = Client.getMessages();
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
