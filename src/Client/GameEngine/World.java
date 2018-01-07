package Client.GameEngine;

import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape;

import static Fields.Constants.*;

public class World extends SubScene {

    private PerspectiveCamera camera;
    private boolean[] WASDLR = new boolean[]{false, false, false, false, false, false};
    private Cylinder player;
    private Group root;
    private Group shapes;

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
        shapes = new Group();
        root.getChildren().add(shapes);

        camera = new PerspectiveCamera(true);
        this.setCamera(camera);
        this.setFill(Color.CADETBLUE);
        camera.setTranslateZ(-50);
        camera.setNearClip(0.1);
        camera.setFarClip(200.0);
        camera.setFieldOfView(40);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        player = new Cylinder(5, 10);
        player.setTranslateZ(camera.getTranslateZ());
        player.setTranslateX(camera.getTranslateX());
        player.setTranslateY(camera.getTranslateY());
//        player.setMaterial(new PhongMaterial(Color.RED));
        root.getChildren().add(player);

        root.setFocusTraversable(true);
        root.requestFocus();
//        for(int z = 0; z < 10; z++)
//            for(int x = 0; x < 10; x++)
//            {
//                Box b = new Box(10, 10, 10);
//                b.setTranslateX(x*b.getWidth());
//                b.setTranslateZ(z*b.getDepth());
//                shapes.getChildren().add(b);
//            }

        Box b1 = new Box(10, 10, 10);
        b1.setMaterial(new PhongMaterial(Color.RED));
        b1.setTranslateZ(0);
        b1.setTranslateX(0);
        b1.setTranslateY(10);
        shapes.getChildren().add(b1);

        Box b2 = new Box(1, 10, 1);
        b2.setMaterial(new PhongMaterial(Color.RED));
        b2.setTranslateZ(0);
        b2.setTranslateX(0);
        b2.setTranslateY(0);
        shapes.getChildren().add(b2);

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

        player.setTranslateX(player.getTranslateX() + step.getX());
        player.setTranslateZ(player.getTranslateZ() + step.getY());

//        camera.setTranslateX(camera.getTranslateX() + step.getX());
//        camera.setTranslateZ(camera.getTranslateZ() + step.getY());

        if(WASDLR[4])
            camera.setRotate((camera.getRotate() - PLAYER_TURN_SPEED)%360);
        if(WASDLR[5])
            camera.setRotate((camera.getRotate() + PLAYER_TURN_SPEED)%360);

        FXCollections.sort(shapes.getChildren(), (Node a, Node b) -> {
            double da = Math.pow((player.getTranslateX() - a.getTranslateX()),2)+Math.pow((player.getTranslateZ() - a.getTranslateZ()),2);
            double db = Math.pow((player.getTranslateX() - b.getTranslateX()),2)+Math.pow((player.getTranslateZ() - b.getTranslateZ()),2);
            return Double.compare(db, da);
        });

        for(Node c : shapes.getChildren())
        {
            if(player.getBoundsInParent().intersects(c.getBoundsInParent()))
            {

                System.out.println("Colliding!");
                        //Subtracting projection does not work with corners.
//                Point2D cp = new Point2D(
//                        c.getTranslateX()-player.getTranslateX(),
//                        c.getTranslateZ()-player.getTranslateZ());
//
//                cp = cp.multiply(cp.dotProduct(step)/cp.dotProduct(cp));
//
//                player.setTranslateX(player.getTranslateX() + cp.getX());
//                player.setTranslateZ(player.getTranslateZ() + cp.getY());

                if(c instanceof Box){
                    // Using the fact that box edges follow z and x planes.
                    double dx = Math.abs(c.getTranslateX()-camera.getTranslateX());
                    double dz = Math.abs(c.getTranslateZ()-camera.getTranslateZ());

                    if(dx < player.getRadius() + ((Box) c).getWidth() || dz < player.getRadius() + ((Box) c).getDepth()) {
                        player.setTranslateX(player.getTranslateX() - step.getX());
                        player.setTranslateZ(player.getTranslateZ() - step.getY());
                        System.out.println("Less than");
                    }
                    else if(dx > dz)
                    {
                        player.setTranslateX(player.getTranslateX() - step.getX());
                    } else if(dx < dz)
                    {
                        player.setTranslateZ(player.getTranslateZ() - step.getY());
                    } else {
                        player.setTranslateX(player.getTranslateX() - step.getX());
                        player.setTranslateZ(player.getTranslateZ() - step.getY());
                    }
                }


            }
        }

        camera.setTranslateX(player.getTranslateX());
        camera.setTranslateZ(player.getTranslateZ());
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
