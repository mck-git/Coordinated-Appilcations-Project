package View;

import Client.Client;
import Templates.TScene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
        chatPanel.getChildren().add(messageArea);

        TextField messageField = new TextField();
        chatPanel.getChildren().add(messageField);

        messageField.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                Client.sendMessage(messageField.getText());
                messageField.clear();
            }
        });

        root.setRight(chatPanel);
    }

    @Override
    public void refresh() {
        String[] newMessages = Client.getMessages();
        if (newMessages.length != messages.length) {
            updateMessageArea(newMessages);
            return;
        }

        for (int i = 0; i < messages.length; i++) {
            if(!newMessages[i].equals(messages[i])) {
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
            chat.append("\n");
        }
        messageArea.setText(chat.toString());
        messageArea.setWrapText(true);
        messageArea.setScrollTop(Double.MAX_VALUE);
    }
}

