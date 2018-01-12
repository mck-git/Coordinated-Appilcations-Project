package Client.Renderer;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class FpsCounter extends Label {

    private long lastUpdate = 0;
    private final int fpsArraySize;
    private final double[] fpsArray;
    private int fpsArrayIndex = 0;

    public FpsCounter()
    {
        fpsArraySize = 60;
        fpsArray = new double[fpsArraySize];
        this.setTextFill(Color.WHITE);
    }

    public void update()
    {
        fpsArray[fpsArrayIndex] = 1e9/(System.nanoTime()- lastUpdate);
        fpsArrayIndex = (fpsArrayIndex + 1) % fpsArraySize;
        lastUpdate = System.nanoTime();
        int averageFps = 0;
        for(double f : fpsArray)
        {
            averageFps += f;
        }
        averageFps /= fpsArraySize;

        this.setText("Fps: " + averageFps);
    }

}
