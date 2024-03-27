package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import levels.Level;
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

    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabbies();
        System.out.println("Size Enemy is: " + crabbies.size());
    }

    public void update(int[][] levelData, Player player) {
        boolean isAnyActive = false;
        for (int i = 0; i < crabbies.size(); i++) {
            if (crabbies.get(i).isActive()) {
                crabbies.get(i).update(levelData, player);
                isAnyActive = true;
            }

        }
        if (!isAnyActive) {
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawCrabs(g, xLevelOffset);
    }

    private void drawCrabs(Graphics g, int xLevelOffset) {
        for (int i = 0; i < crabbies.size(); i++) {
            if (crabbies.get(i).isActive()) {
                g.drawImage(crabbyArray[crabbies.get(i).getEnemyState()][crabbies.get(i).getAniIndex()],
                        (int) crabbies.get(i).getHitbox().x - xLevelOffset - CRABBY_DRAW_OFFSET_X
                                + crabbies.get(i).flipX(),
                        (int) crabbies.get(i).getHitbox().y - CRABBY_DRAW_OFFSET_Y,
                        CRABBY_WIDTH * crabbies.get(i).flipW(),
                        CRABBY_HEIGHT, null);
                // crabbies.get(i).drawAttackBox(g, xLevelOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (int i = 0; i < crabbies.size(); i++) {
            if (crabbies.get(i).isActive()) {
                if (attackBox.intersects(crabbies.get(i).getHitbox())) {
                    crabbies.get(i).hurt(150);
                    return;
                }
            }
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

    public void resetAllEnemies() {
        for (int i = 0; i < crabbies.size(); i++) {
            crabbies.get(i).resetEnemy();
        }
    }
}
