package Client.GameEngine;

import javafx.scene.control.Label;

public class FpsCounter extends Label {

    private long lastupdate = 0;
    private final int fpsArraySize;
    private final double[] fpsArray;
    private int fpsArrayIndex = 0;

    public FpsCounter(int size)
    {
        fpsArraySize = size;
        fpsArray = new double[fpsArraySize];
    }
    public FpsCounter()
    {
        fpsArraySize = 20;
        fpsArray = new double[fpsArraySize];
    }

    public void update()
    {
        fpsArray[fpsArrayIndex] = 1000/(System.currentTimeMillis()-lastupdate);
        fpsArrayIndex = (fpsArrayIndex + 1) % fpsArraySize;
        lastupdate = System.currentTimeMillis();
        int averageFps = 0;
        for(double f : fpsArray)
        {
            averageFps += f;
        }
        averageFps /= fpsArraySize;

        this.setText("Fps: " + averageFps);
    }

}
