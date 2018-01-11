package Client.Renderer;

import Client.ClientApp;
import Client.Networking.MainConnector;
import Client.Networking.RoomConnector;
import Shared.GameState;
import Shared.Map;
import Shared.Player;
import Shared.PlayerInfo;
import javafx.collections.FXCollections;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;

import static Shared.Constants.*;

public class World extends SubScene {

    private PerspectiveCamera camera;
//    private boolean[] WASDLRS = new boolean[]{false, false, false, false, false, false,false};
    private PointLight torch;
    private Group root;
    private Group renderings;
    private Player user;
    private Map map;

    public World() {
        super(new Group(), WIDTH, HEIGHT);
        this.root = (Group) getRoot();
        setup();
    }

    private void setup()
    {
        this.map = new Map();

        Rectangle floor = new Rectangle(1000, 1000, Color.grayRgb(30));
        floor.setTranslateY(-floor.getHeight()/2);
        floor.setTranslateX(-floor.getWidth()/2);
        floor.setRotationAxis(new Point3D(1, 0, 0));
        floor.setRotate(90);
        floor.setCacheHint(CacheHint.SCALE_AND_ROTATE);
        floor.setCache(true);
        root.getChildren().add(floor);

        this.heightProperty().bind(ClientApp.getStage().heightProperty());
        this.widthProperty().bind(ClientApp.getStage().widthProperty());

        renderings = new Group();
        root.getChildren().add(renderings);

        user = new Player(new PlayerInfo(MainConnector.getUserName()));
        root.getChildren().add(user);

        camera = new PerspectiveCamera(true);
        this.setCamera(camera);
        this.setFill(Color.BLUE);
//        camera.setTranslateX(user.getTranslateX());
        camera.setTranslateY(-0.9*user.getHeight());
//        camera.setTranslateZ(user.getTranslateZ());

        camera.translateXProperty().bind(user.translateXProperty());
        camera.translateZProperty().bind(user.translateZProperty());
        camera.setNearClip(0.1);
        camera.setFarClip(500);
        camera.setFieldOfView(40);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        AmbientLight ambient = new AmbientLight(Color.color(0.3, 0.3, 0.3));
        root.getChildren().add(ambient);

        torch = new PointLight(Color.color(0.5, 0.5, 0.3));
        torch.setTranslateY(camera.getTranslateY() * 2);
        root.getChildren().add(torch);
        torch.setRotationAxis(camera.getRotationAxis());

        torch.translateXProperty().bind(user.translateXProperty().multiply(2));
        torch.translateZProperty().bind(user.translateZProperty());

        root.setFocusTraversable(true);
        root.requestFocus();
        renderings.getChildren().addAll(map.getNodes());

        Box b1 = new Box(TILE_SIZE/3, TILE_SIZE, TILE_SIZE/3);
        b1.setMaterial(new PhongMaterial(Color.color(0.4, 0, 0.2, 0.5)));
        b1.setTranslateZ(-TILE_SIZE);
        b1.setTranslateX(-TILE_SIZE);
        b1.setTranslateY(-0.5*TILE_SIZE);
        renderings.getChildren().add(b1);

//        this.setOnKeyPressed(this::keyPressedListeners);
//        this.setOnKeyReleased(this::keyReleasedListeners);
        this.setOnKeyPressed(key -> RoomConnector.setKeyPress(key.getCode(), true));
        this.setOnKeyReleased(key -> RoomConnector.setKeyPress(key.getCode(), false));
    }

    public void update(GameState gs)
    {
//        double angle = Math.toRadians(camera.getRotate());
//        Point2D direction = new Point2D(Math.sin(angle), Math.cos(angle));
//        Point2D perpDirection = new Point2D(direction.getY(), -direction.getX());
//        Point2D step = Point2D.ZERO;
//
//        if(WASDLRS[0])
//            step = step.add(direction);
//        if(WASDLRS[1])
//            step = step.subtract(perpDirection);
//        if(WASDLRS[2])
//            step = step.subtract(direction);
//        if(WASDLRS[3])
//            step = step.add(perpDirection);
//        if(WASDLRS[4])
//            camera.setRotate((camera.getRotate() - PLAYER_TURN_SPEED)%360);
//        if(WASDLRS[5])
//            camera.setRotate((camera.getRotate() + PLAYER_TURN_SPEED)%360);
//
//        step = step.normalize().multiply(PLAYER_SPEED);
//
//        player.setTranslateX(player.getTranslateX() + step.getX());
//        player.setTranslateZ(player.getTranslateZ() + step.getY());
//
//        Node c;
//        for(int i = renderings.getChildren().size()-1; i >= Math.max(0, renderings.getChildren().size() - 5); i--)
//        {
//            c = renderings.getChildren().get(i);
//            if(player.getBoundsInParent().intersects(c.getBoundsInParent()))
//            {
//                        //Subtracting projection does not work with corners.
////                Point2D cp = new Point2D(
////                        c.getTranslateX()-player.getTranslateX(),
////                        c.getTranslateZ()-player.getTranslateZ());
////
////                cp = cp.multiply(cp.dotProduct(step)/cp.dotProduct(cp));
////
////                player.setTranslateX(player.getTranslateX() + cp.getX());
////                player.setTranslateZ(player.getTranslateZ() + cp.getY());
//
//                //Subtract colliding part of vector, using that edges of box follow x and z planes
////                if(c instanceof Box){
////                    // Using the fact that box edges follow z and x planes.
////                    double dx = Math.abs(c.getTranslateX()-camera.getTranslateX());
////                    double dz = Math.abs(c.getTranslateZ()-camera.getTranslateZ());
////
////                    if(dx < player.getRadius() + ((Box) c).getWidth() || dz < player.getRadius() + ((Box) c).getDepth()) {
////                        player.setTranslateX(player.getTranslateX() - step.getX());
////                        player.setTranslateZ(player.getTranslateZ() - step.getY());
////                        System.out.println("Less than");
////                    }
////                    else if(dx > dz)
////                    {
////                        player.setTranslateX(player.getTranslateX() - step.getX());
////                    } else if(dx < dz)
//
////                    {
////                        player.setTranslateZ(player.getTranslateZ() - step.getY());
////                    } else {
////                        player.setTranslateX(player.getTranslateX() - step.getX());
////                        player.setTranslateZ(player.getTranslateZ() - step.getY());
////                    }
////                }
////
//                // Using that boundsinparent is a close fitting rectangle.
//                // Player turned into a box instead of a cylinder
//                // Using that walls are made of square boxes with equal side lengths.
//                double dx = Math.abs(c.getTranslateX() - player.getTranslateX());
//                double dz = Math.abs(c.getTranslateZ() - player.getTranslateZ());
//                if (dz > dx) {
//                    player.setTranslateZ(c.getTranslateZ() - Math.signum(c.getTranslateZ() - camera.getTranslateZ())*0.5*(TILE_SIZE+PLAYER_SIZE+0.01));
//                }
//                if (dx > dz) {
//                    player.setTranslateX(c.getTranslateX() - Math.signum(c.getTranslateX() - camera.getTranslateX())*0.5*(TILE_SIZE+PLAYER_SIZE+0.01));
//                }
//            }
//        }
//
//        camera.setTranslateX(player.getTranslateX());
//        camera.setTranslateZ(player.getTranslateZ());
//
//        torch.setTranslateX(camera.getTranslateX()*2);
//        torch.setTranslateZ(camera.getTranslateZ());


        for(Node p : renderings.getChildren())
        {
            if(p instanceof Player)
                ((Player) p).active = false;
        }

        for(PlayerInfo pi : gs.getPlayer_infos())
        {

            if(user.is(pi.username)){
                user.update(pi);
                camera.setRotate(pi.angle);
                if(pi.fire)
                    addShot(pi.x, pi.z, pi.angle);
                continue;
            }

            Player player = null;

            for(Node p: renderings.getChildren())
            {
                if(p instanceof Player && ((Player) p).is(pi.username))
                {
                    player = ((Player)p);
                    break;
                }
            }

            if(player != null)
            {
                //update
                player.update(pi);
                if(pi.fire)
                    addShot(pi.x, pi.z, pi.angle);
            } else{
                System.out.println("Found new player: " + pi.username);
                renderings.getChildren().add(new Player(pi));
            }
        }

        for(int i = renderings.getChildren().size()-1 ; i >= 0 ; i--)
        {
            if(renderings.getChildren().get(i) instanceof Player && !((Player)renderings.getChildren().get(i)).active){
                System.out.println("Player left: " + ((Player)renderings.getChildren().get(i)).username);
                renderings.getChildren().remove(i);
            }

            if(renderings.getChildren().get(i) instanceof Cylinder){
                Cylinder shot = (Cylinder) renderings.getChildren().get(i);
                Color color = ((PhongMaterial) shot.getMaterial()).getDiffuseColor();
                if(color.getOpacity() < 0.1)
                    renderings.getChildren().remove(i);
                else
                    ((PhongMaterial) shot.getMaterial()).setDiffuseColor(Color.color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity() - 0.05));
            }
        }

        sortRenderingsByEuclidianDistance();
    }

//    private void keyPressedListeners(KeyEvent key)
//    {
//        switch (key.getCode()) {
//            case W:
//                WASDLRS[0] = true;
//                RoomConnector.setKeyPress("forward",true);
//                break;
//            case A:
//                WASDLRS[1] = true;
//                RoomConnector.setKeyPress("left",true);
//                break;
//            case S:
//                WASDLRS[2] = true;
//                RoomConnector.setKeyPress("back",true);
//                break;
//            case D:
//                WASDLRS[3] = true;
//                RoomConnector.setKeyPress("right",true);
//                break;
//            case LEFT:
//                WASDLRS[4] = true;
//                RoomConnector.setKeyPress("rotateLeft",true);
//                break;
//            case RIGHT:
//                WASDLRS[5] = true;
//                RoomConnector.setKeyPress("rotateRight",true);
//                break;
//            case SPACE:
//                WASDLRS[6] = true;
//                RoomConnector.setKeyPress("fire",true);
//
//        }
//    }
//
//    private void keyReleasedListeners(KeyEvent key)
//    {
//        switch (key.getCode()) {
//            case W:
//                WASDLRS[0] = false;
//                RoomConnector.setKeyPress("forward",false);
//                break;
//            case A:
//                WASDLRS[1] = false;
//                RoomConnector.setKeyPress("left",false);
//                break;
//            case S:
//                WASDLRS[2] = false;
//                RoomConnector.setKeyPress("back",false);
//                break;
//            case D:
//                WASDLRS[3] = false;
//                RoomConnector.setKeyPress("right",false);
//                break;
//            case LEFT:
//                WASDLRS[4] = false;
//                RoomConnector.setKeyPress("rotateLeft",false);
//                break;
//            case RIGHT:
//                WASDLRS[5] = false;
//                RoomConnector.setKeyPress("rotateRight",false);
//                break;
//            case SPACE:
//                WASDLRS[6] = false;
//                RoomConnector.setKeyPress("fire",false);
//        }
//    }

    private void sortRenderingsByEuclidianDistance() {
        FXCollections.sort(renderings.getChildren(), (Node a, Node b) -> {
            double da = Math.pow((user.getTranslateX() - a.getTranslateX()),2)+Math.pow((user.getTranslateZ() - a.getTranslateZ()),2);
            double db = Math.pow((user.getTranslateX() - b.getTranslateX()),2)+Math.pow((user.getTranslateZ() - b.getTranslateZ()),2);
            return Double.compare(db, da);
        });
    }

    private void addShot(double x, double z, int angle)
    {
        double radians = Math.toRadians(angle);
        Point3D direction = new Point3D(Math.sin(radians), 0, Math.cos(radians));

        double height = 0;
        double sx = x+0.5*TILE_SIZE, sz = z+0.5*TILE_SIZE;
        for(int i = 0; i < 10; i++)
        {
            if(sx < 0 || sz < 0)
                continue;

            if(map.grid[(int) (sz/TILE_SIZE)][(int)(sx/TILE_SIZE)] == 1)
            {
                break;
            }
            sx += SHOT_INTERPOLATION_INTERVAL *direction.getX();
            sz += SHOT_INTERPOLATION_INTERVAL *direction.getZ();
            height += 10;
        }

        Cylinder shot = new Cylinder();
        shot.setTranslateY(camera.getTranslateY()*0.95);
        shot.setRotationAxis(new Point3D(direction.getZ(), 0, -direction.getX()));
        shot.setRotate(-90);
        shot.setRadius(SHOT_RADIUS);
        shot.setHeight(height);
        shot.setTranslateX(x + direction.multiply(0.5*shot.getHeight()+1).getX() + 0.2*direction.getZ());
        shot.setTranslateZ(z + direction.multiply(0.5*shot.getHeight()+1).getZ() - 0.2*direction.getX());
        shot.setMaterial(new PhongMaterial(Color.color(0.2, 0.4, 1, 0.9)));
        renderings.getChildren().add(shot);
//        System.out.println("Shot " + height + " (" + sx + ", " + sz + ")");
    }
}
