package Client.Renderer;

import Shared.PlayerInfo;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class KillDeathRatio extends Label {

    double currentRatio;

    public KillDeathRatio(String type)
    {

        if (type.equals("user"))
        {
            this.setText("KDR: " + currentRatio);
            this.setTextFill(Color.RED);
        }

        if (type.equals("top"))
        {
            this.setText("Top KDR: " + currentRatio);
            this.setTextFill(Color.GOLD);
        }


        currentRatio = 0.0;
    }

    public void update(PlayerInfo p)
    {
        currentRatio = p.calculateKDR();


    }
}
