package ui;

import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;
import static utilz.Constants.UI.UrmButtons.URM_BUTTONS_SIZE;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import gamestates.GameState;
import gamestates.Playing;
import utilz.LoadSave;

public class LevelCompleteOverlay {
    private Playing playing;
    private UrmButtons menu, next;
    private BufferedImage image;
    private int bgX, bgY, bgWidth, bgHeight;

    public LevelCompleteOverlay(Playing playing) {
        this.playing = playing;
        initLevelCompleteImage();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (330 * SCALES);
        int nextX = (int) (445 * SCALES);
        int y = (int) (195 * SCALES);
        next = new UrmButtons(nextX, y, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 0);
        menu = new UrmButtons(menuX, y, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 2);
    }

    private void initLevelCompleteImage() {
        image = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETE);
        bgWidth = (int) (image.getWidth() * SCALES);
        bgHeight = (int) (image.getHeight() * SCALES);
        bgX = GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (75 * SCALES);

    }

    public void draw(Graphics g) {
        g.drawImage(image, bgX, bgY, bgWidth, bgHeight, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update() {
        next.update();
        menu.update();
    }

    private boolean isIn(UrmButtons buttons, MouseEvent e) {
        return buttons.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                GameState.state = GameState.MENU;
            }
        } else if (isIn(next, e)) {
            if (next.isMousePressed()) {
              playing.loadNextLevel();
            }
        }
        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            menu.setMousePressed(true);
        } else if (isIn(next, e)) {
            next.setMousePressed(true);
        }
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);
        if (isIn(menu, e)) {
            menu.setMouseOver(true);
        } else if (isIn(next, e)) {
            next.setMouseOver(true);
        }
    }
}
