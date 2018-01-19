package Shared;

import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;
import java.util.Random;

import static Shared.Constants.TILE_SIZE;

public class Map {
    private Random random = new Random();
    private Node[][] nodeGrid;
    public final int[][] grid = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public Map()
    {
        nodeGrid = new Node[depth()][width()];
        for(int z = 0; z < grid.length; z++) {
            for (int x = 0; x < grid[z].length; x++) {
                if (grid[z][x] == 1) {
                    Box b = new Box(TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    b.setMaterial(new PhongMaterial());
                    ((PhongMaterial) b.getMaterial()).setDiffuseMap(new Image("Shared/Resources/concrete.png"));
                    b.setTranslateX(x * TILE_SIZE);
                    b.setTranslateZ(z * TILE_SIZE);
                    b.setTranslateY(-0.5 * TILE_SIZE);
                    b.setCacheHint(CacheHint.SCALE_AND_ROTATE);
                    b.setCache(true);
                    nodeGrid[z][x] = b;
                }
            }
        }
    }

    public int width()
    {
        return grid[0].length;
    }

    public int depth()
    {
        return grid.length;
    }

    public Point2D getNewRespawnLocation()
    {
        int x;
        int z;
        do
        {
            z = random.nextInt(grid.length - 2) + 1;
            x = random.nextInt(grid[0].length - 2) + 1;

        } while (grid[z][x] != 0);

        return new Point2D(x * TILE_SIZE,z * TILE_SIZE);
    }

    public Node[][] getNodeGrid()
    {
        return nodeGrid;
    }

    public ArrayList<Node> getNodeList() {
        ArrayList<Node> walls = new ArrayList<>();

        for (int z = 0; z < depth(); z++) {
            for (int x = 0; x < width(); x++) {
                if (nodeGrid[z][x] != null) {
                    walls.add(nodeGrid[z][x]);
                }
            }
        }
        return walls;
    }

    public ArrayList<Node> getSurroundingNodes(int px, int pz)
    {
        ArrayList<Node> neighbors = new ArrayList<>();

        for(int z = pz-1; z <= pz+1; z++)
            for(int x = px-1; x <= px+1; x++)
                if(z >= 0 && x >= 0 && z < depth() && x < width() && nodeGrid[z][x] != null)
                    neighbors.add(nodeGrid[z][x]);

        return neighbors;
    }
}
