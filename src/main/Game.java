package main;

import java.awt.Graphics;
import gamestates.GameState;
import gamestates.Menu;
import gamestates.Playing;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    private final int FPS_SET = 120;
    private Thread gameThread;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;

    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALES = 1.2f;
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALES);
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int GAME_WIDTH = TILES_IN_WIDTH * TILES_SIZE;
    public static final int GAME_HEIGHT = TILES_IN_HEIGHT * TILES_SIZE;

    public Game() {
        initClass();
        gamePanel = new GamePanel(this);
        gamePanel.requestFocus();
        gameWindow = new GameWindow(gamePanel);

        startGameLoop();

    }

    private void initClass() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {

        switch (GameState.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
                break;
            default:
                System.exit(0);
                break;
        }

    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }

    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;

        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }

    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}
