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

import static utilz.Constants.Environment.*;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
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
    private int levelTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = levelTilesWide * Game.TILES_IN_WIDTH;
    private int maxLevelOffsetX = maxTilesOffset * Game.TILES_SIZE;
    private Random rnd = new Random();
    private int[] smallCloudPos;
    private BufferedImage backgroundImgPlaying, imageCloud, imageSmallCloud;

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
    }

    private void initClass() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (64 * SCALES), (int) (40 * SCALES));
        player.loadLevelData(levelManager.getCurrLevel().getCurrLevelData());
        pauseOverPlay = new PauseOverPlay(this);
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrLevel().getCurrLevelData());
            checkCloseToBoder();
        } else {
            pauseOverPlay.update();
        }

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

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPlaying, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        drawClouds(g);
        levelManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyManager.draw(g, xLevelOffset);
        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
            pauseOverPlay.draw(g);

        }

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

        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }

    }

    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverPlay.mouseDragged(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverPlay.mouseReleased(e);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverPlay.mousePressed(e);
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverPlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                player.setDirection(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                player.setDirection(false);
                break;

            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
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

    public void unpauseGame() {
        paused = false;
    }
}
