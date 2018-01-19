package Client.Renderer;

import Shared.PlayerInfo;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class KillDeathRatio extends Label {

    private double currentRatio;
    private double currentTopRatio;
    private String pname;

    public KillDeathRatio()
    {
        pname = "";

        this.setText("KDR: " + currentRatio + "\n");
        this.setTextFill(Color.RED);
        this.setText("Top KDR: " + currentTopRatio + "\n"
        + "Top player: " + pname);
        this.setTextFill(Color.GOLD);


        currentRatio = 0.0;
        currentTopRatio = 0.0;
    }

    public void update(PlayerInfo p, PlayerInfo top)
    {
        currentRatio = p.calculateKDR();
        currentTopRatio = top.calculateKDR();
        pname = top.username;

        this.setTextFill(Color.RED);
        this.setText("KDR: " + String.format("%.2f",currentRatio) + "\n" + "Top KDR: " + String.format("%.2f",currentTopRatio) + "\n"
                + "Top player: " + pname);
        this.setTextFill(Color.GOLD);

    }
}
