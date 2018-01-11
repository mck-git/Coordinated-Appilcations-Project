package Client.Renderer;

import Shared.PlayerInfo;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class KillDeathRatio extends Label {

    private double currentRatio;
    private String pname;

    public KillDeathRatio(String type)
    {
        pname = "";

        if (type.equals("user"))
        {
            this.setText("KDR: " + currentRatio);
            this.setTextFill(Color.RED);
        }

        if (type.equals("top"))
        {
            this.setText("Top KDR: " + currentRatio + "\n"
            + "Top player: " + pname);
            this.setTextFill(Color.GOLD);
        }


        currentRatio = 0.0;
    }

    public void update(PlayerInfo p)
    {
        currentRatio = p.calculateKDR();
        pname = p.username;
    }
}
