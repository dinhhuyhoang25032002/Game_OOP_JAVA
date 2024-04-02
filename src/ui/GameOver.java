package ui;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;
import static utilz.Constants.UI.UrmButtons.URM_BUTTONS_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import audio.PlayerAudio;

import java.awt.event.MouseEvent;

import gamestates.GameState;
import gamestates.Playing;
import utilz.LoadSave;

public class GameOver {
    private Playing playing;
    private BufferedImage img;
    private int imageX, imageY, imageW, imageH;
    private UrmButtons menu, play;

    public GameOver(Playing playing) {
        this.playing = playing;
        createImg();
        initButtons();
    }

    private void initButtons() {
        int  menuX= (int) (335 * SCALES);
        int playX = (int) (440 * SCALES);
        int y = (int) (195 * SCALES);
        play = new UrmButtons(playX, y, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 1);
        menu = new UrmButtons(menuX, y, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 2);
    }

    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imageW = (int) (img.getWidth() * SCALES);
        imageH = (int) (img.getHeight() * SCALES);
        imageX = GAME_WIDTH / 2 - imageW / 2;
        imageY = (int) (100 * SCALES);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        g.drawImage(img, imageX, imageY, imageW, imageH, null);
        menu.draw(g);
        play.draw(g);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }

    public void update() {
        play.update();
        menu.update();
    }

    private boolean isIn(UrmButtons buttons, MouseEvent e) {
        return buttons.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.getGame().getPlayerAudio().stopSong();
               // playing.getGame().getPlayerAudio().
                playing.getGame().getPlayerAudio().playSong(PlayerAudio.MENU_1);
                GameState.state = GameState.MENU;

            }
        } else if (isIn(play, e)) {
            if (play.isMousePressed()) {
               playing.resetAll();
            }
        }
        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            menu.setMousePressed(true);
        } else if (isIn(play, e)) {
            play.setMousePressed(true);
        }
    }

    public void mouseMoved(MouseEvent e) {
        play.setMouseOver(false);
        menu.setMouseOver(false);
        if (isIn(menu, e)) {
            menu.setMouseOver(true);
        } else if (isIn(play, e)) {
            play.setMouseOver(true);
        }
    }
}
