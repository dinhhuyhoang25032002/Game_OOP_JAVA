package levels;

import static main.Game.GAME_WIDTH;
import static main.Game.TILES_IN_WIDTH;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.GameState;
import main.Game;
import utilz.LoadSave;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        ImportOutsideSprites();
        levels = new ArrayList<Level>();
        buildAllLevel();

    }

    public void loadNextLevel() {
        levelIndex++;
        if (levelIndex >= levels.size()) {
            levelIndex = 0;
            System.out.println("No more levels! Game completed!");
            GameState.state = GameState.MENU;
        }
        Level newLevel = levels.get(levelIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getCurrLevelData());
        game.getPlaying().setMaxLevelOffset(newLevel.getMaxLevelOffsetX());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevel() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (int i = 0; i < allLevels.length; i++) {
            levels.add(new Level(allLevels[i]));
        }
    }

    private void ImportOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                int index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int levelOffset) {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; i++) {
            for (int j = 0; j < levels.get(levelIndex).getCurrLevelData()[0].length; j++) {
                int index = levels.get(levelIndex).GetSpriteIndex(i, j);
                g.drawImage(levelSprite[index], j * Game.TILES_SIZE - levelOffset, i * Game.TILES_SIZE,
                        Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrLevel() {
        return levels.get(levelIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }
}
