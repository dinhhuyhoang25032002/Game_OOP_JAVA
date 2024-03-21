package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import entities.Crabby;
import static main.Game.TILES_SIZE;
import static utilz.Constants.EnemyConstants.CRABBY;
import java.awt.Color;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
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
    public static final String STATUS_BAS = "health_power_bar.png";
    public static final String LEVEL_COMPLETE = "completed_sprite.png";

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

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/map");
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
        }
        BufferedImage[] images = new BufferedImage[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                images[i] = ImageIO.read(files[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }
}
