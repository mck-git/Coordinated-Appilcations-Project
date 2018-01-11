package Client.Renderer;

import Shared.PlayerInfo;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class KillDeathRatio extends Label {

    private double currentRatio;
    private String pname;
    String type;

    public KillDeathRatio(String type)
    {
        pname = "";
        this.type = type;

        if (this.type.equals("user"))
        {
            this.setText("KDR: " + currentRatio);
            this.setTextFill(Color.RED);
        }

        if (this.type.equals("top"))
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


        if (this.type.equals("user"))
        {
            this.setText("KDR: " + currentRatio);
            this.setTextFill(Color.RED);
        }

        if (this.type.equals("top"))
        {
            this.setText("Top KDR: " + currentRatio + "\n"
                    + "Top player: " + pname);
            this.setTextFill(Color.GOLD);
        }

    }
}
