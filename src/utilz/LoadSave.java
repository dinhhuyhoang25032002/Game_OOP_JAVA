package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Crabby;
import main.Game;

import static main.Game.TILES_SIZE;
import static utilz.Constants.EnemyConstants.CRABBY;

import java.awt.Color;

public class LoadSave {
    public static final String PLAYER_ATLAS_RIGHT = "player_sprites.png";
    public static final String PLAYER_ATLAS_LEFT = "flipped_image.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    // public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String BUTTON_MENU = "button_atlas.png";
    public static final String BACKGROUND_MENU = "menu_background.png";
    public static final String PAUSE_MENU = "pause_menu.png";
    public static final String SOUND_BUTTON = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUD_IMG = "big_clouds.png";
    public static final String SMALL_CLOUD = "small_clouds.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return img;
    }

    public static ArrayList<Crabby> GetCrabs() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Crabby> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getGreen();
                if (value == CRABBY) {
                    list.add(new Crabby(j * TILES_SIZE, i * TILES_SIZE));
                }

            }
        }
        return list;
    }

    public static int[][] GetLevelData() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        int[][] levelData = new int[img.getHeight()][img.getWidth()];

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                levelData[i][j] = value;
            }
        }
        return levelData;
    }
}
