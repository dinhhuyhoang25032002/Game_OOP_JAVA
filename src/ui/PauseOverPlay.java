package ui;

import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;
import static utilz.Constants.UI.PauseButton.SOUND_BUTTON_SIZE;
import static utilz.Constants.UI.UrmButtons.*;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Playing;

import java.awt.event.MouseEvent;

import utilz.LoadSave;

public class PauseOverPlay {
    private BufferedImage background;
    private int bgpX, bgpY, bgpwidth, bgpHeight;
    private SoundButton musicButton, sfxButton;
    private UrmButtons menuB, replayB, unpauseB;
    private Playing playing;
    private VolumeButtons volumeButtons;
    public PauseOverPlay(Playing playing) {
        this.playing = playing;
        loadPauseBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        int vX = (int) (309 * SCALES);
        int vY = (int) (278 * SCALES);
        volumeButtons = new VolumeButtons(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
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

    private void createSoundButtons() {
        int soundX = (int) (450 * SCALES);
        int musicY = (int) (140 * SCALES);
        int sfxY = (int) (186 * SCALES);
        musicButton = new SoundButton(soundX, musicY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
    }

    private void loadPauseBackground() {
        background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU);
        bgpwidth = (int) (background.getWidth() * SCALES);
        bgpHeight = (int) (background.getHeight() * SCALES);
        bgpX = GAME_WIDTH / 2 - bgpwidth / 2;
        bgpY = (int) (25 * SCALES);

    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButtons.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgpX, bgpY, bgpwidth, bgpHeight, null);

        musicButton.draw(g);
        sfxButton.draw(g);

        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        volumeButtons.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButtons.isMousePressed()) {
            volumeButtons.ChangeX(e.getX());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        } else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                System.out.println("Replay level!");
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unpauseGame();
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();

        volumeButtons.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);

        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        }else if (isIn(e, volumeButtons)) {
            volumeButtons.setMousePressed(true);
        }

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);

        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
        else if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        }else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        }
        else if (isIn(e, volumeButtons)) {
            volumeButtons.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }
}
