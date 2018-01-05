package View;

import Client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jspace.Space;

import static Fields.Constants.HEIGHT;
import static Fields.Constants.WIDTH;


public class WelcomeMenu extends Scene{

    public WelcomeMenu(BorderPane bp) {
        super(bp, WIDTH, HEIGHT);
        bp.setStyle("-fx-background-image: url(\"Images/steam_train_blue_background_by_keno9988-d6gt3pk.png\");-fx-background-size: "+WIDTH*2+", "+HEIGHT*2+";-fx-background-repeat: repeat;");

        bp.setPadding(new Insets(10,50,50,50));

        HBox hb = new HBox();
        hb.setPadding(new Insets(20,20,20,30));

        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Implementing Nodes for GridPane
        Label lblUserName = new Label("Username");
        final TextField txtUserName = new TextField();
        Label lblErrorMessage = new Label();
        Button btnLogin = new Button("Login");

        //Adding Nodes to GridPane layout
        gridPane.add(lblErrorMessage, 1, 0);
        gridPane.add(lblUserName, 0, 1);
        gridPane.add(txtUserName, 1, 1);
        gridPane.add(btnLogin, 1, 2);

        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);

        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);

        //Adding text and DropShadow effect to it
        Text text = new Text("Welcome");
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
        text.setEffect(dropShadow);

        //Adding text to HBox
        hb.getChildren().add(text);
        hb.setAlignment(Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);

        setOnKeyPressed(key -> {
            if(key.getCode() == KeyCode.ENTER)
            {
                btnLogin.fire();
            }
        });

        btnLogin.setOnAction(event -> {
            if (Client.initialize(txtUserName.getText()))
            {
                lblErrorMessage.setText("Welcome " + txtUserName.getText() + "!");
            }
            else
            {
                lblErrorMessage.setText("Username invalid or taken");
            }
        });

        //Add HBox and GridPane layout to BorderPane Layout
        bp.setTop(hb);
        bp.setCenter(gridPane);
    }
}
