package Client.GameEngine;

import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;

import static Fields.Constants.*;

public class World extends SubScene {

    private PerspectiveCamera camera;
    private Cylinder player;
    private boolean[] WASDLR = new boolean[]{false, false, false, false, false, false};
    private Group root;

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

    private void setup()
    {
        camera = new PerspectiveCamera(true);
        this.setCamera(camera);
        this.setFill(Color.CADETBLUE);

        camera.setTranslateZ(-50);
        camera.setNearClip(0.1);
        camera.setFarClip(200.0);
        camera.setFieldOfView(40);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        player = new Cylinder(5, 5);
        player.setTranslateZ(camera.getTranslateZ());
        player.setTranslateX(camera.getTranslateX());
        player.setTranslateY(camera.getTranslateY());

        root.setFocusTraversable(true);
        root.requestFocus();

        for(int i = 0; i < 20; i++)
        {
            Box b = new Box(10, 10, 10);
            b.setTranslateX(i*b.getWidth());
            b.setTranslateY(0);
            b.setTranslateZ(0);
            root.getChildren().add(b);
        }
        this.setOnKeyPressed(this::keyPressedListeners);
        this.setOnKeyReleased(this::keyReleasedListeners);
    }

    public void update()
    {
        double angle = Math.toRadians(camera.getRotate());
        Point2D direction = new Point2D(Math.sin(angle), Math.cos(angle));
        Point2D perpDirection = new Point2D(direction.getY(), -direction.getX());
        Point2D step = Point2D.ZERO;

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
            double da = Math.pow((camera.getTranslateX() - a.getTranslateX()),2)+Math.pow((camera.getTranslateZ() - a.getTranslateZ()),2);
            double db = Math.pow((camera.getTranslateX() - b.getTranslateX()),2)+Math.pow((camera.getTranslateZ() - b.getTranslateZ()),2);
            return Double.compare(db, da);
        });

        player.setTranslateZ(camera.getTranslateZ());
        player.setTranslateX(camera.getTranslateX());
        player.setTranslateY(camera.getTranslateY());

        for(Node n : root.getChildren())
        {
            if(player.getBoundsInParent().intersects(n.getBoundsInParent()))
            {
                camera.setTranslateX(camera.getTranslateX() - step.getX());
                camera.setTranslateZ(camera.getTranslateZ() - step.getY());
                player.setTranslateZ(camera.getTranslateZ());
                player.setTranslateX(camera.getTranslateX());
                player.setTranslateY(camera.getTranslateY());
            }
        }
    }

    private void keyPressedListeners(KeyEvent key)
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

    private void keyReleasedListeners(KeyEvent key)
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
