package Shared;

import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;

import static Shared.Constants.TILE_SIZE;

public class Map {
    public static final int[][] grid = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public static ArrayList<Node> getNodes()
    {
        ArrayList<Node> walls = new ArrayList();

        for(int z = 0; z < grid.length; z++)
            for(int x = 0; x < grid[z].length; x++)
            {
                if(grid[z][x] == 1)
                {
                    Box b = new Box(TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    b.setMaterial(new PhongMaterial());
                    ((PhongMaterial) b.getMaterial()).setDiffuseMap(new Image("Shared/Resources/concrete.png"));
                    b.setTranslateX(x * TILE_SIZE);
                    b.setTranslateZ(z * TILE_SIZE);
                    b.setTranslateY(-0.5 * TILE_SIZE);
                    b.setCacheHint(CacheHint.SCALE_AND_ROTATE);
                    b.setCache(true);
                    walls.add(b);
                }
            }

        return walls;
    }


}
