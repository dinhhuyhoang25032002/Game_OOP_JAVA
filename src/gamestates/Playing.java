package gamestates;

import static main.Game.SCALES;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverPlay;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    private boolean paused = true;
    private PauseOverPlay pauseOverPlay;

    public Playing(Game game) {
        super(game);
        initClass();
    }

    private void initClass() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * SCALES), (int) (40 * SCALES));
        player.loadLevelData(levelManager.getCurrLevel().getCurrLevelData());
        pauseOverPlay = new PauseOverPlay();
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {
        levelManager.update();
        player.update();
        pauseOverPlay.update();
    }

    @Override
    public void draw(Graphics g) {

        levelManager.draw(g);
        player.render(g);
        pauseOverPlay.draw(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
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
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;

            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                GameState.state = GameState.MENU;
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
}
