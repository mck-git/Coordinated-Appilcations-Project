package Client.UI;

import Client.ClientApp;
import Client.Networking.MainConnector;
import Templates.TScene;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static Shared.Constants.HEIGHT;
import static Shared.Constants.WIDTH;

public class WelcomeMenu extends TScene {

    private ComboBox<String> serverView = new ComboBox<>();
    private Button btnLogin;

    public WelcomeMenu() {
        super(new BorderPane());
    }

    @Override
    public void setup() {
        BorderPane root = (BorderPane) getRoot();
        root.setStyle("-fx-background-image: url(Shared/Resources/steam_train_blue_background_by_keno9988-d6gt3pk.png);-fx-background-size: "+WIDTH*2+", "+HEIGHT*2+";-fx-background-repeat: repeat;");

        root.setPadding(new Insets(10,10,50,10));

        VBox topPane = new VBox();
        HBox quitPane = new HBox();
        Button exit = new Button("Exit Game");
        exit.setFocusTraversable(false);
        quitPane.getChildren().add(exit);
        topPane.getChildren().add(quitPane);
        exit.setOnAction(event -> MainConnector.exitApplication());

        HBox hb = new HBox();
        topPane.getChildren().add(hb);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label lblUserName = new Label("Username");
        final TextField txtUserName = new TextField();
        Label lblErrorMessage = new Label();
        btnLogin = new Button("Login");

        Label lblChooseServer = new Label("Choose server: ");
        serverView.setMinWidth(100);

        gridPane.add(lblErrorMessage, 1, 0);
        gridPane.add(lblUserName, 0, 1);
        gridPane.add(txtUserName, 1, 1);
        gridPane.add(btnLogin, 1, 2);
        gridPane.add(lblChooseServer, 0, 3);
        gridPane.add(serverView, 1, 3);

        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);

        Text text = new Text("Welcome");
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
        text.setEffect(dropShadow);

        hb.getChildren().add(text);
        hb.setAlignment(Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);

        setOnKeyPressed(key -> {
            switch(key.getCode())
            {
                case ENTER:
                    btnLogin.fire();
                    break;
                case ESCAPE:
                    exit.fire();
                    break;
            }
        });

        btnLogin.setOnAction(event -> {
            if (serverView.getSelectionModel() != null &&
                    MainConnector.initialize(txtUserName.getText(), serverView.getSelectionModel().getSelectedItem()))
            {
                ClientApp.setScene(new Lobby());
            }
            else
            {
                lblErrorMessage.setText("Username invalid or taken");
            }
        });

        btnLogin.setOnKeyPressed(key -> {
            switch(key.getCode()) {
                case ENTER:
                    btnLogin.fire();
                    break;
            }
        });

        root.setTop(topPane);
        root.setCenter(gridPane);
    }

    @Override
    public void refresh() {
        try {
            serverView.setItems(FXCollections.observableArrayList(MainConnector.foundServers));
            if (!serverView.getItems().isEmpty() && serverView.getSelectionModel().getSelectedItem() == null)
                serverView.getSelectionModel().select(0);

            if (serverView.getSelectionModel().getSelectedItem() == null)
                btnLogin.setDisable(true);
            else if (btnLogin.isDisabled())
                btnLogin.setDisable(false);
        } catch (Exception ignored){}
    }

    @Override
    public void closingProtocol() {
        MainConnector.exitApplication();
    }

    @Override
    public void leavingProtocol() {
        //Nothing
    }
}
