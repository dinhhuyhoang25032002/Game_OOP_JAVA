package ui;

import static main.Game.GAME_WIDTH;
import static main.Game.SCALES;
import static utilz.Constants.UI.PauseButton.SOUND_BUTTON_SIZE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.awt.event.MouseEvent;

import utilz.LoadSave;

public class PauseOverPlay {
    private BufferedImage background;
    private int bgpX, bgpY, bgpwidth, bgpHeight;
    private SoundButton musicButton, sfxButton;

    public PauseOverPlay() {
        loadPauseBackground();
        createSoundButtons();
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
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgpX, bgpY, bgpwidth, bgpHeight, null);

        musicButton.draw(g);
        sfxButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

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
        }
        musicButton.resetBools();
        sfxButton.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);

        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        }

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);

        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }
}
