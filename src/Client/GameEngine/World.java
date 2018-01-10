package Client.GameEngine;

import Client.GameClient;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import static Fields.Constants.*;

public class World extends SubScene {

    private PerspectiveCamera camera;
    private boolean[] WASDLRS = new boolean[]{false, false, false, false, false, false,false};
    private Box player;
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

        player = new Box(PLAYER_SIZE, 6, PLAYER_SIZE);

        camera = new PerspectiveCamera(true);
        this.setCamera(camera);
        this.setFill(Color.CADETBLUE);
        camera.setTranslateZ(-50);
        camera.setNearClip(0.1);
        camera.setFarClip(200.0);
        camera.setFieldOfView(40);
        camera.setRotationAxis(new Point3D(0, 1, 0));
        camera.setTranslateY(-0.9*player.getHeight());

        player.setTranslateZ(camera.getTranslateZ());
        player.setTranslateX(camera.getTranslateX());
        player.setTranslateY(camera.getTranslateY());
//        player.setMaterial(new PhongMaterial(Color.RED));
        root.getChildren().add(player);

        root.setFocusTraversable(true);
        root.requestFocus();
        for(int z = 0; z < 10; z++)
            for(int x = 0; x < 10; x++)
            {
                if(Math.random() < 0.5)
                {
                    Box b = new Box(TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    b.setTranslateX(x * TILE_SIZE);
                    b.setTranslateZ(z * TILE_SIZE);
                    b.setTranslateY(-0.5*TILE_SIZE);
                    shapes.getChildren().add(b);
                }
            }

        Box b1 = new Box(TILE_SIZE, TILE_SIZE, TILE_SIZE);
        b1.setMaterial(new PhongMaterial(Color.RED));
        b1.setTranslateZ(-TILE_SIZE);
        b1.setTranslateX(-TILE_SIZE);
        b1.setTranslateY(-0.5*TILE_SIZE);
        shapes.getChildren().add(b1);

        this.setOnKeyPressed(this::keyPressedListeners);
        this.setOnKeyReleased(this::keyReleasedListeners);
    }

    public void update()
    {
        double angle = Math.toRadians(camera.getRotate());
        Point2D direction = new Point2D(Math.sin(angle), Math.cos(angle));
        Point2D perpDirection = new Point2D(direction.getY(), -direction.getX());
        Point2D step = Point2D.ZERO;

        if(WASDLRS[0])
            step = step.add(direction);
        if(WASDLRS[1])
            step = step.subtract(perpDirection);
        if(WASDLRS[2])
            step = step.subtract(direction);
        if(WASDLRS[3])
            step = step.add(perpDirection);
        if(WASDLRS[4])
            camera.setRotate((camera.getRotate() - PLAYER_TURN_SPEED)%360);
        if(WASDLRS[5])
            camera.setRotate((camera.getRotate() + PLAYER_TURN_SPEED)%360);

        step = step.normalize().multiply(PLAYER_SPEED);

        player.setTranslateX(player.getTranslateX() + step.getX());
        player.setTranslateZ(player.getTranslateZ() + step.getY());

//        camera.setTranslateX(camera.getTranslateX() + step.getX());
//        camera.setTranslateZ(camera.getTranslateZ() + step.getY());


//        double sx = step.getX();
//        double sz = step.getY();
//        boolean s = false;
        Node c;
        for(int i = shapes.getChildren().size()-1; i >= Math.max(0, shapes.getChildren().size() - 5); i--)
        {
            c = shapes.getChildren().get(i);
            if(player.getBoundsInParent().intersects(c.getBoundsInParent()))
            {
                        //Subtracting projection does not work with corners.
//                Point2D cp = new Point2D(
//                        c.getTranslateX()-player.getTranslateX(),
//                        c.getTranslateZ()-player.getTranslateZ());
//
//                cp = cp.multiply(cp.dotProduct(step)/cp.dotProduct(cp));
//
//                player.setTranslateX(player.getTranslateX() + cp.getX());
//                player.setTranslateZ(player.getTranslateZ() + cp.getY());

                //Subtract colliding part of vector, using that edges of box follow x and z planes
//                if(c instanceof Box){
//                    // Using the fact that box edges follow z and x planes.
//                    double dx = Math.abs(c.getTranslateX()-camera.getTranslateX());
//                    double dz = Math.abs(c.getTranslateZ()-camera.getTranslateZ());
//
//                    if(dx < player.getRadius() + ((Box) c).getWidth() || dz < player.getRadius() + ((Box) c).getDepth()) {
//                        player.setTranslateX(player.getTranslateX() - step.getX());
//                        player.setTranslateZ(player.getTranslateZ() - step.getY());
//                        System.out.println("Less than");
//                    }
//                    else if(dx > dz)
//                    {
//                        player.setTranslateX(player.getTranslateX() - step.getX());
//                    } else if(dx < dz)

//                    {
//                        player.setTranslateZ(player.getTranslateZ() - step.getY());
//                    } else {
//                        player.setTranslateX(player.getTranslateX() - step.getX());
//                        player.setTranslateZ(player.getTranslateZ() - step.getY());
//                    }
//                }
//
                // Using that boundsinparent is a close fitting rectangle.
                // Player turned into a box instead of a cylinder
                // Using that walls are made of square boxes with equal side lengths.
                double dx = Math.abs(c.getTranslateX() - player.getTranslateX());
                double dz = Math.abs(c.getTranslateZ() - player.getTranslateZ());
                if (dz > dx) {
                    player.setTranslateZ(c.getTranslateZ() - Math.signum(c.getTranslateZ() - camera.getTranslateZ())*0.5*(TILE_SIZE+PLAYER_SIZE+0.01));
                }
                if (dx > dz) {
                    player.setTranslateX(c.getTranslateX() - Math.signum(c.getTranslateX() - camera.getTranslateX())*0.5*(TILE_SIZE+PLAYER_SIZE+0.01));
                }
            }
        }

        camera.setTranslateX(player.getTranslateX());
        camera.setTranslateZ(player.getTranslateZ());
//        camera.setTranslateZ(camera.getTranslateZ() + sz);
//        camera.setTranslateX(camera.getTranslateX() + sx);

//        player.setTranslateX(camera.getTranslateX());
//        player.setTranslateZ(camera.getTranslateZ());

        sortShapesByEuclidianDistance();
    }

    private void keyPressedListeners(KeyEvent key)
    {
        switch (key.getCode()) {
            case W:
                WASDLRS[0] = true;
                GameClient.setKeyPress("forward",true);
                break;
            case A:
                WASDLRS[1] = true;
                GameClient.setKeyPress("left",true);
                break;
            case S:
                WASDLRS[2] = true;
                GameClient.setKeyPress("back",true);
                break;
            case D:
                WASDLRS[3] = true;
                GameClient.setKeyPress("right",true);
                break;
            case LEFT:
                WASDLRS[4] = true;
                GameClient.setKeyPress("rotateLeft",true);
                break;
            case RIGHT:
                WASDLRS[5] = true;
                GameClient.setKeyPress("rotateRight",true);
                break;
            case SPACE:
                WASDLRS[6] = true;
                GameClient.setKeyPress("fire",true);

        }
    }

    private void keyReleasedListeners(KeyEvent key)
    {
        switch (key.getCode()) {
            case W:
                WASDLRS[0] = false;
                GameClient.setKeyPress("forward",false);
                break;
            case A:
                WASDLRS[1] = false;
                GameClient.setKeyPress("left",false);
                break;
            case S:
                WASDLRS[2] = false;
                GameClient.setKeyPress("back",false);
                break;
            case D:
                WASDLRS[3] = false;
                GameClient.setKeyPress("right",false);
                break;
            case LEFT:
                WASDLRS[4] = false;
                GameClient.setKeyPress("rotateLeft",false);
                break;
            case RIGHT:
                WASDLRS[5] = false;
                GameClient.setKeyPress("rotateRight",false);
                break;
            case SPACE:
                WASDLRS[6] = false;
                GameClient.setKeyPress("fire",false);
        }
    }

    private void sortShapesByEuclidianDistance() {
        FXCollections.sort(shapes.getChildren(), (Node a, Node b) -> {
            double da = Math.pow((player.getTranslateX() - a.getTranslateX()),2)+Math.pow((player.getTranslateZ() - a.getTranslateZ()),2);
            double db = Math.pow((player.getTranslateX() - b.getTranslateX()),2)+Math.pow((player.getTranslateZ() - b.getTranslateZ()),2);
            return Double.compare(db, da);
        });
    }
}
