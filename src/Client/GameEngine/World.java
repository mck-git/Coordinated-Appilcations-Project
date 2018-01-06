package Client.GameEngine;

import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static Fields.Constants.*;

public class World extends SubScene {

    private PerspectiveCamera camera;
    private boolean[] WASDLR = new boolean[]{false, false, false, false, false, false};
    private Group root;
    private long time;

    public World(Group root, double width, double height) {
        super(root, width, height);
        this.root = root;
        setup();
    }
    public World(Group root) {
        this(root, WIDTH, HEIGHT);
    }
    public World() {
        this(new Group());
    }

    public void setup()
    {
        camera = new PerspectiveCamera(true);
        this.setCamera(camera);

        this.setFill(Color.CADETBLUE);

        camera.setTranslateZ(-50);
        camera.setNearClip(0.1);
        camera.setFarClip(200.0);
        camera.setFieldOfView(40);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        root.setFocusTraversable(true);
        root.requestFocus();

        for(int i = 0; i < 20; i++)
        {
            Box b = new Box(10, 10, 10);
            b.setTranslateX(i*b.getWidth());
            b.setTranslateY(0);
            b.setTranslateZ(0);
            root.getChildren().add(b);

            Box b1 = new Box(10, 10, 10);
            b1.setTranslateX(i*b1.getWidth());
            b1.setTranslateY(0);
            b1.setTranslateZ(-100);
            root.getChildren().add(b1);
        }

        this.setOnKeyPressed(this::keyPressedListeners);
        this.setOnKeyReleased(this::keyReleasedListeners);
    }

    public void updateCamera()
    {

        System.out.println(1000 / (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();

        double angle = camera.getRotate() * Math.PI / 180;
        Point2D direction = new Point2D(Math.sin(angle), Math.cos(angle));
        Point2D perpDirection = new Point2D(direction.getY(), -direction.getX());
        Point2D step = Point2D.ZERO;

//        System.out.println(
//                String.format("[%.2f, %.2f] : [%.2f, %.2f] - Angle: %.2f",
//                direction.getX(), direction.getY(),
//                perpDirection.getX(), perpDirection.getY(),
//                angle)
//        );

        if(WASDLR[0])
            step = step.add(direction);
        if(WASDLR[1])
            step = step.subtract(perpDirection);
        if(WASDLR[2])
            step = step.subtract(direction);
        if(WASDLR[3])
            step = step.add(perpDirection);

        step = step.normalize().multiply(PLAYER_SPEED);

        camera.setTranslateX(camera.getTranslateX() + step.getX());
        camera.setTranslateZ(camera.getTranslateZ() + step.getY());

        if(WASDLR[4])
            camera.setRotate((camera.getRotate() - PLAYER_TURN_SPEED)%360);
        if(WASDLR[5])
            camera.setRotate((camera.getRotate() + PLAYER_TURN_SPEED)%360);

        FXCollections.sort(root.getChildren(), (Node a, Node b) -> {
            double da = new Point2D(a.getTranslateX(), a.getTranslateZ()).distance(camera.getTranslateX(), camera.getTranslateZ());
            double db = new Point2D(b.getTranslateX(), b.getTranslateZ()).distance(camera.getTranslateX(), camera.getTranslateZ());
            return db > da ? 1 : db == da ? 0 : -1;
        });
    }

    public void keyPressedListeners(KeyEvent key)
    {
        switch (key.getCode()) {
            case W:
                WASDLR[0] = true;
                break;
            case A:
                WASDLR[1] = true;
                break;
            case S:
                WASDLR[2] = true;
                break;
            case D:
                WASDLR[3] = true;
                break;
            case LEFT:
                WASDLR[4] = true;
                break;
            case RIGHT:
                WASDLR[5] = true;
                break;
        }
    }

    public void keyReleasedListeners(KeyEvent key)
    {
        switch (key.getCode()) {
            case W:
                WASDLR[0] = false;
                break;
            case A:
                WASDLR[1] = false;
                break;
            case S:
                WASDLR[2] = false;
                break;
            case D:
                WASDLR[3] = false;
                break;
            case LEFT:
                WASDLR[4] = false;
                break;
            case RIGHT:
                WASDLR[5] = false;
                break;
        }
    }
}
