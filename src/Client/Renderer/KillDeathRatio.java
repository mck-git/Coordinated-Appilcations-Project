package Client.Renderer;

import Shared.PlayerInfo;


import javafx.scene.control.Label;

public class KillDeathRatio extends Label {

    double currentRatio;

    public KillDeathRatio()
    {
        currentRatio = 0.0;
    }

    public void update(PlayerInfo p)
    {
        currentRatio = p.calculateKDR();

        this.setText("KDR: " + currentRatio);
    }

}
