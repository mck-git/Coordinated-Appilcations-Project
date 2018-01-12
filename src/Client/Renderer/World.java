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
        root.getChildren().add(floor);
        floor.setTranslateY(-floor.getHeight()/2);
        floor.setTranslateX(-floor.getWidth()/2);
        floor.setRotationAxis(new Point3D(1, 0, 0));
        floor.setRotate(90);
        floor.setCacheHint(CacheHint.SCALE_AND_ROTATE);
        floor.setCache(true);

        this.heightProperty().bind(ClientApp.getStage().heightProperty());
        this.widthProperty().bind(ClientApp.getStage().widthProperty());
        this.setFill(Color.BLUE);

        renderings = new Group();
        root.getChildren().add(renderings);

        user = new Player(new PlayerInfo(MainConnector.getUserName()));
        root.getChildren().add(user);

        camera = new PerspectiveCamera(true);
        this.setCamera(camera);
        camera.setTranslateY(-0.9*user.getHeight());
        camera.translateXProperty().bind(user.translateXProperty());
        camera.translateZProperty().bind(user.translateZProperty());
        camera.setNearClip(0.1);
        camera.setFarClip(500);
        camera.setFieldOfView(40);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        AmbientLight ambient = new AmbientLight(Color.color(0.3, 0.3, 0.3));
        root.getChildren().add(ambient);

        torch = new PointLight(Color.color(0.5, 0.5, 0.3));
        root.getChildren().add(torch);
        torch.setTranslateY(camera.getTranslateY() * 2);
        torch.setRotationAxis(camera.getRotationAxis());
        torch.translateXProperty().bind(user.translateXProperty().multiply(2));
        torch.translateZProperty().bind(user.translateZProperty());

        root.setFocusTraversable(true);
        root.requestFocus();

        renderings.getChildren().addAll(map.getNodeList());

        Box b1 = new Box(TILE_SIZE/3, TILE_SIZE, TILE_SIZE/3);
        b1.setMaterial(new PhongMaterial(Color.color(0.4, 0, 0.2, 0.5)));
        b1.setTranslateZ(-TILE_SIZE);
        b1.setTranslateX(-TILE_SIZE);
        b1.setTranslateY(-0.5*TILE_SIZE);
        renderings.getChildren().add(b1);

        this.setOnKeyPressed(key -> RoomConnector.setKeyPress(key.getCode(), true));
        this.setOnKeyReleased(key -> RoomConnector.setKeyPress(key.getCode(), false));
    }

    public void update(GameState gs)
    {
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
                if(pi.fire || pi.fireLastFrame)
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
                player.update(pi);
                if(pi.fire || pi.fireLastFrame)
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
                    ((PhongMaterial) shot.getMaterial())
                            .setDiffuseColor(
                                    Color.color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity() - 0.05));
            }
        }

        sortRenderingsByEuclidianDistance();
    }

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

        double sx = x+0.5*TILE_SIZE, sz = z+0.5*TILE_SIZE;
        double range = 0;
        int steps = (map.depth()*TILE_SIZE + map.width()*TILE_SIZE)/SHOT_INTERPOLATION_INTERVAL;
        for(int i = 0; i < steps; i++)
        {
            if(sz >= 0 &&
                    sx >= 0 &&
                    sz < map.depth()*TILE_SIZE &&
                    sx < map.width()*TILE_SIZE &&
                    map.grid[(int) (sz/TILE_SIZE)][(int)(sx/TILE_SIZE)] == 1)
                break;

            sx += SHOT_INTERPOLATION_INTERVAL *direction.getX();
            sz += SHOT_INTERPOLATION_INTERVAL *direction.getZ();
            range += SHOT_INTERPOLATION_INTERVAL;
        }

        Cylinder shot = new Cylinder();
        shot.setTranslateY(camera.getTranslateY() + SHOT_OFFSET);
        shot.setRotationAxis(new Point3D(direction.getZ(), 0, -direction.getX()));
        shot.setRotate(-90);
        shot.setRadius(SHOT_RADIUS);
        shot.setHeight(range);
        shot.setTranslateX(x + direction.multiply(0.5*shot.getHeight()+1).getX());
        shot.setTranslateZ(z + direction.multiply(0.5*shot.getHeight()+1).getZ());
        shot.setMaterial(new PhongMaterial(Color.color(0.2, 0.4, 1, 0.9)));
        renderings.getChildren().add(shot);
    }
}
