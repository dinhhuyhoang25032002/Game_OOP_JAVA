package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayerAudio {
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LEVEL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;
    public static int SFX_BUTTON = 7;
    public static int CANNON = 8;
    public static int DESTROY_BOX = 9;
    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random Rnd = new Random();

    public PlayerAudio() {
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }

    private void loadSongs() {
        String[] names = { "menu", "level1", "level2" };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }
    }

    private void loadEffects() {
        String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3", "sfx",
                "cannon", "destroy_box" };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(effectNames[i]);
            // System.out.println("hoang check data: " + effects[i]);
        }
        updateEffectVolume();
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (int i = 0; i < songs.length; i++) {
            BooleanControl booleanControl = (BooleanControl) songs[i].getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (int i = 0; i < effects.length; i++) {
            BooleanControl booleanControl = (BooleanControl) effects[i].getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
            // System.out.println("hoàng check data: "+ effects[i]);
        }
        if (!effectMute) {
            playEffect(SFX_BUTTON);
        }

    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive()) {
            songs[currentSongId].stop();
        }
    }

   
    public void setLevelSong(int levelIndex) {
        if (levelIndex % 2 == 0) {
            playSong(LEVEL_1);
        } else {
            playSong(LEVEL_2);
        }
    }

    public void levelCompleted() {
        stopSong();
        playEffect(LEVEL_COMPLETED);
    }

    public void playAttackSound() {
        int start = 4;
        start += Rnd.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playSong(int song) {
        stopSong();
        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        Float rangeVolume = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (rangeVolume * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectVolume() {
        for (int i = 0; i < effects.length; i++) {
            FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
            Float rangeVolume = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (rangeVolume * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

}
