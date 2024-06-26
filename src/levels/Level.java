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
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

public class Level {
    private int[][] levelData;
    private BufferedImage image;
    private ArrayList<Crabby> crabbies;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage image) {
        this.image = image;
        createLevelData();
        createEnemies();
        createPotion();
        createContainer();
        createSpike();
        createCannon();
        calcLevelOffsets();
        calcPlayerSpawn();
    }

    private void createCannon() {
        cannons = HelpMethods.GetCannon(image);
    }

    private void createSpike() {
        spikes = HelpMethods.GetSpikes(image);
    }

    private void createPotion() {
        potions = HelpMethods.GetPotion(image);
    }

    private void createContainer() {
        containers = HelpMethods.GetContainers(image);
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

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

}
