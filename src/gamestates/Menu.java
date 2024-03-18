package gamestates;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButtons;
import utilz.LoadSave;

public class Menu extends State implements StateMethods {
    private MenuButtons[] buttons = new MenuButtons[3];
    private BufferedImage image, backgroundMenu;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButton();
        loadBackgroundImage();
        backgroundMenu = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackgroundImage() {
        image = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_MENU);
        menuWidth = (int) (image.getWidth() * SCALES);
        menuHeight = (int) (image.getHeight() * SCALES);
        menuX = GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * SCALES);
    }

    private void loadButton() {

        buttons[0] = new MenuButtons(GAME_WIDTH / 2, (int) (150 * SCALES), 0, GameState.PLAYING);
        buttons[1] = new MenuButtons(GAME_WIDTH / 2, (int) (220 * SCALES), 1, GameState.OPTIONS);
        buttons[2] = new MenuButtons(GAME_WIDTH / 2, (int) (290 * SCALES), 2, GameState.QUIT);

    }

    @Override
    public void update() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].update();
        }

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundMenu, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(image, menuX, menuY, menuWidth, menuHeight, null);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            if (isIn(e, buttons[i])) {
                if (buttons[i].getMousePressed()) {
                    buttons[i].applyGameState();
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        try {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].resetMouse();
            }
        } catch (Exception e) {

            throw new UnsupportedOperationException("Unimplemented method 'resetButtons'");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            if (isIn(e, buttons[i])) {
                buttons[i].setMousePressed(true);
                break;
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setMouseOver(false);
        }
        for (int i = 0; i < buttons.length; i++) {
            if (isIn(e, buttons[i])) {
                buttons[i].setMouseOver(true);
                break;
            }

        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}
