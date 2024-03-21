package levels;

import static main.Game.TILES_IN_WIDTH;
import static main.Game.TILES_SIZE;
import static utilz.HelpMethods.GetCrabs;
import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetPlayerSpawn;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import entities.Crabby;
import main.Game;

public class Level {
    private int[][] levelData;
    private BufferedImage image;
    private ArrayList<Crabby> crabbies;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage image) {
        this.image = image;
        createLevelData();
        createEnemies();
        calcLevelOffsets();
        calcPlayerSpawn();
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(image);
    }

    private void calcLevelOffsets() {
        levelTilesWide = image.getWidth();
        maxTilesOffset = levelTilesWide - TILES_IN_WIDTH;
        maxLevelOffsetX = TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabbies = GetCrabs(image);
    }

    public ArrayList<Crabby> getCrabbies() {
        return crabbies;
    }

    public int getMaxLevelOffsetX() {
        return maxLevelOffsetX;
    }

    private void createLevelData() {
        levelData = GetLevelData(image);
    }

    public int GetSpriteIndex(int x, int y) {
        return levelData[x][y];
    }

    public int[][] getCurrLevelData() {
        return levelData;
    }

}
