package levels;

import static main.Game.GAME_WIDTH;
import static main.Game.TILES_IN_WIDTH;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        // levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        ImportOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());

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
            for (int j = 0; j < levelOne.getCurrLevelData()[0].length; j++) {
                int index = levelOne.GetSpriteIndex(i, j);
                g.drawImage(levelSprite[index], j * Game.TILES_SIZE - levelOffset, i * Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        Game.TILES_SIZE, null);
            }
        }

    }

    public void update() {

    }

    public Level getCurrLevel() {
        return levelOne;
    }
}
