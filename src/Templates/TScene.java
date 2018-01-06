package Templates;

import javafx.scene.Parent;
import javafx.scene.Scene;

import static Fields.Constants.HEIGHT;
import static Fields.Constants.WIDTH;

public abstract class TScene extends Scene {
    public TScene(Parent root) {
        super(root, WIDTH, HEIGHT);
    }

    public abstract void setup();
    public abstract void refresh();
    public abstract void closingProtocol();
    public abstract void leavingProtocol();
}
