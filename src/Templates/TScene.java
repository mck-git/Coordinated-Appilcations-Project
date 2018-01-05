package Templates;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import static Fields.Constants.*;

public abstract class TScene extends Scene {
    public TScene() {
        super(new BorderPane(), WIDTH, HEIGHT);
    }

    public abstract void setup();
    public abstract void refresh();
    public abstract void closingProtocol();
}
