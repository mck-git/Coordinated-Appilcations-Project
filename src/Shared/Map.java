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
        int row, col;

        for(int y = 0; y < grid.length; y++)
            for(int x = 0; x < grid[y].length; x++)
            {
                if(grid[y][x] == 1)
                {
                    row = y - grid.length/2;
                    col = x - grid[y].length/2;
                    Box b = new Box(TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    b.setMaterial(new PhongMaterial());
                    ((PhongMaterial) b.getMaterial()).setDiffuseMap(new Image("Shared/Resources/concrete.png"));
                    b.setTranslateX(row * TILE_SIZE);
                    b.setTranslateZ(col * TILE_SIZE);
                    b.setTranslateY(-0.5 * TILE_SIZE);
                    b.setCacheHint(CacheHint.SCALE_AND_ROTATE);
                    b.setCache(true);
                    walls.add(b);
                }
            }

        return walls;
    }


}
