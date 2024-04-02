package ui;

import static main.Game.SCALES;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.PauseButton.SOUND_BUTTON_SIZE;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;
import java.awt.Graphics;

import java.awt.event.MouseEvent;

import gamestates.GameState;
import main.Game;

public class AudioOptions {
    private VolumeButtons volumeButtons;
    private SoundButton musicButton, sfxButton;
    private Game game;

    public AudioOptions(Game game) {
        this.game = game;
        createSoundButtons();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        int vX = (int) (309 * SCALES);
        int vY = (int) (278 * SCALES);
        volumeButtons = new VolumeButtons(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = (int) (450 * SCALES);
        int musicY = (int) (140 * SCALES);
        int sfxY = (int) (186 * SCALES);
        musicButton = new SoundButton(soundX, musicY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeButtons.update();
    }

    public void draw(Graphics g) {
        musicButton.draw(g);
        sfxButton.draw(g);
        volumeButtons.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButtons.isMousePressed()) {
            float valueBefore = volumeButtons.getFloatValue();
            volumeButtons.ChangeX(e.getX());
            float valueAfter = volumeButtons.getFloatValue();
            if (valueBefore != valueAfter) {
                game.getPlayerAudio().setVolume(valueAfter);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                game.getPlayerAudio().toggleSongMute();
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getPlayerAudio().toggleEffectMute();
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButtons.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, volumeButtons)) {
            volumeButtons.setMousePressed(true);
        }

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
       // volumeButton.setMouseOver(false);
        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);

        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isIn(e, volumeButtons)) {
            volumeButtons.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }
}
