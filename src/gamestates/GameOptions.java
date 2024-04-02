package gamestates;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;
import static utilz.Constants.UI.UrmButtons.URM_BUTTONS_SIZE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButtons;
import utilz.LoadSave;

public class GameOptions extends State implements StateMethods {
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButtons menuB;

    public GameOptions(Game game) {
        super(game);
        loadImg();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    private void loadButton() {
        int menuX = (int) (387 * SCALES);
        int menuY = (int) (325 * SCALES);
        menuB = new UrmButtons(menuX, menuY, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 2);
    }

    private void loadImg() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BACKGROUND);
        bgW = (int) (optionBackgroundImg.getWidth() * SCALES);
        bgH = (int) (optionBackgroundImg.getHeight() * SCALES);
        bgX = GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * SCALES);
    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(optionBackgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed())
                GameState.state = GameState.MENU;
        } else {
            audioOptions.mouseReleased(e);
        }
        menuB.resetBools();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }

}
