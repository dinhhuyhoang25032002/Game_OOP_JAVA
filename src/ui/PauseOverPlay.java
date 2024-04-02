package ui;

import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;
import static utilz.Constants.UI.PauseButton.SOUND_BUTTON_SIZE;
import static utilz.Constants.UI.UrmButtons.*;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import audio.PlayerAudio;
import gamestates.GameState;
import gamestates.Playing;

import java.awt.event.MouseEvent;

import utilz.LoadSave;

public class PauseOverPlay {
    private BufferedImage background;
    private int bgpX, bgpY, bgpwidth, bgpHeight;
    private UrmButtons menuB, replayB, unpauseB;
    private Playing playing;
    private AudioOptions audioOptions;

    public PauseOverPlay(Playing playing) {
        this.playing = playing;
        loadPauseBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();

    }

    private void createUrmButtons() {
        int menuX = (int) (313 * SCALES);
        int replayX = (int) (387 * SCALES);
        int unpauseX = (int) (462 * SCALES);
        int bY = (int) (325 * SCALES);
        menuB = new UrmButtons(menuX, bY, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 2);
        replayB = new UrmButtons(replayX, bY, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 1);
        unpauseB = new UrmButtons(unpauseX, bY, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, 0);
    }

    private void loadPauseBackground() {
        background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU);
        bgpwidth = (int) (background.getWidth() * SCALES);
        bgpHeight = (int) (background.getHeight() * SCALES);
        bgpX = GAME_WIDTH / 2 - bgpwidth / 2;
        bgpY = (int) (25 * SCALES);

    }

    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgpX, bgpY, bgpwidth, bgpHeight, null);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
                playing.getGame().getPlayerAudio().stopSong();
                playing.getGame().getPlayerAudio().playSong(PlayerAudio.MENU_1);
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unpauseGame();
            }
        } else {
            audioOptions.mouseReleased(e);
        }
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        }
        if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }

    }

    public void mouseMoved(MouseEvent e) {

        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }
}
