package Client.Renderer;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Crosshair extends Group {
    public Crosshair(int lineLength, int centerDistance, int lineWidth, int yOffset, Color color)
    {
        Line caTop = new Line(0, -centerDistance, 0, -(centerDistance + lineLength));
        caTop.setStroke(color);
        caTop.setStrokeWidth(lineWidth);

        Line caBottom = new Line(0, centerDistance, 0, (centerDistance + lineLength));
        caBottom.setStroke(color);
        caBottom.setStrokeWidth(lineWidth);

        Line caLeft = new Line(-centerDistance, 0, -(centerDistance + lineLength), 0);
        caLeft.setStroke(color);
        caLeft.setStrokeWidth(lineWidth);

        Line caRight = new Line(centerDistance, 0, (centerDistance + lineLength), 0);
        caRight.setStroke(color);
        caRight.setStrokeWidth(lineWidth);

        this.getChildren().addAll(caTop, caBottom, caLeft, caRight);
        this.setTranslateY(yOffset);
    }
    public Crosshair()
    {
        this(8, 5, 2, 5, Color.color(0.8, 0.8, 0.8, 0.6));
    }
}
