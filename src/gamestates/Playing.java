package gamestates;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import static utilz.Constants.Environment.*;
import ui.GameOver;
import ui.LevelCompleteOverlay;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.PauseOverPlay;
import utilz.LoadSave;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    private boolean paused = false;
    private PauseOverPlay pauseOverPlay;
    private EnemyManager enemyManager;
    private int xLevelOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLevelOffsetX;

    private Random rnd = new Random();
    private int[] smallCloudPos;
    private BufferedImage backgroundImgPlaying, imageCloud, imageSmallCloud;
    private boolean GameOver;
    private GameOver gameOver;
    private LevelCompleteOverlay levelCompleteOverlay;
    private boolean levelCompleted = false;
    private ObjectManager objectManager;

    public Playing(Game game) {
        super(game);
        initClass();
        backgroundImgPlaying = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        imageCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUD_IMG);
        imageSmallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUD);
        smallCloudPos = new int[8];
        for (int i = 0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int) (70 * SCALES) + rnd.nextInt((int) (100 * SCALES));
        }
        calcLevelOffsets();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrLevel().getPlayerSpawn());

    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrLevel());
        objectManager.loadObjects(levelManager.getCurrLevel());
    }

    private void calcLevelOffsets() {
        maxLevelOffsetX = levelManager.getCurrLevel().getMaxLevelOffsetX();

    }

    private void initClass() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (64 * SCALES), (int) (40 * SCALES), this);
        player.loadLevelData(levelManager.getCurrLevel().getCurrLevelData());
        player.setSpawn(levelManager.getCurrLevel().getPlayerSpawn());

        pauseOverPlay = new PauseOverPlay(this);
        gameOver = new GameOver(this);
        levelCompleteOverlay = new LevelCompleteOverlay(this);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean GameOver) {
        this.GameOver = GameOver;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {

        if (paused) {
            pauseOverPlay.update();
        } else if (levelCompleted) {
            levelCompleteOverlay.update();
        } else if (!GameOver) {
            levelManager.update();
            objectManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrLevel().getCurrLevelData(), player);
            checkCloseToBoder();
        }

    }

    public void resetAll() {
        GameOver = false;
        paused = false;
        player.resetAll();
        levelCompleted = false;
        enemyManager.resetAllEnemies();
        objectManager.resetAll();
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    private void checkCloseToBoder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLevelOffset;
        if (diff > rightBorder) {
            xLevelOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLevelOffset += diff - leftBorder;
        }

        if (xLevelOffset > maxLevelOffsetX) {
            xLevelOffset = maxLevelOffsetX;
        } else if (xLevelOffset < 0) {
            xLevelOffset = 0;
        }
    }

    public void checkPotionTouched(Rectangle2D.Float hitBox) {
        objectManager.checkObjectTouched(hitBox);
    }

    public void checkObjecthitbox(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouched(player);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPlaying, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        drawClouds(g);

        levelManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyManager.draw(g, xLevelOffset);
        objectManager.draw(g, xLevelOffset);

        if (paused && !GameOver) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
            pauseOverPlay.draw(g);
        } else if (GameOver) {
            gameOver.draw(g);
        } else if (levelCompleted) {
            levelCompleteOverlay.draw(g);
        }
    }

    public void setMaxLevelOffset(int levelOffset) {
        this.maxLevelOffsetX = levelOffset;
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 2; i++) {
            g.drawImage(imageCloud, i * BIG_CLOUD_WIDTH - (int) (xLevelOffset * 0.01), (int) (200 * SCALES),
                    BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT,
                    null);
        }
        for (int i = 0; i < smallCloudPos.length; i++) {
            g.drawImage(imageSmallCloud, SMALL_CLOUD_WIDTH * i * 3 - (int) (xLevelOffset * 0.7), smallCloudPos[i],
                    SMALL_CLOUD_WIDTH,
                    SMALL_CLOUD_HEIGHT, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!GameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!GameOver) {
            if (paused) {
                pauseOverPlay.mouseDragged(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!GameOver) {
            if (paused) {
                pauseOverPlay.mouseReleased(e);
            } else if (levelCompleted) {
                levelCompleteOverlay.mouseReleased(e);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!GameOver) {
            if (paused) {
                pauseOverPlay.mousePressed(e);
            } else if (levelCompleted) {
                levelCompleteOverlay.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!GameOver) {
            if (paused) {
                pauseOverPlay.mouseMoved(e);
            } else if (levelCompleted) {
                levelCompleteOverlay.mouseMoved(e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (GameOver) {
            gameOver.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;

                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
            }
        }
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!GameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;

                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void unpauseGame() {
        paused = false;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

}
