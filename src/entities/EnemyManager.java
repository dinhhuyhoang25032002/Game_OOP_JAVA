package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

import gamestates.Playing;
import utilz.LoadSave;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArray;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;

        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.GetCrabs();
        System.out.println("Size Enemy is: " + crabbies.size());
    }

    public void update(int[][] levelData) {
        for (int i = 0; i < crabbies.size(); i++) {
            crabbies.get(i).update(levelData);
        }
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawCrabs(g, xLevelOffset);
    }

    private void drawCrabs(Graphics g, int xLevelOffset) {
        for (int i = 0; i < crabbies.size(); i++) {
            g.drawImage(crabbyArray[crabbies.get(i).getEnemyState()][crabbies.get(i).getAniIndex()],
                    (int) crabbies.get(i).getHitbox().x - xLevelOffset - CRABBY_DRAW_OFFSET_X,
                    (int) crabbies.get(i).getHitbox().y - CRABBY_DRAW_OFFSET_Y, CRABBY_WIDTH,
                    CRABBY_HEIGHT, null);
        }

    }

    private void loadEnemyImgs() {
        crabbyArray = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int i = 0; i < crabbyArray.length; i++) {
            for (int j = 0; j < crabbyArray[i].length; j++) {
                crabbyArray[i][j] = temp.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT,
                        CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }
}
