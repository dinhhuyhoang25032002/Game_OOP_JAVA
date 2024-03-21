package entities;

import static main.Game.SCALES;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Crabby extends Enemy {

    private Rectangle2D.Float attackBox;
    private int attackBoxOffseX;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * SCALES), (int) (19 * SCALES));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (77 * SCALES), (int) (19 * SCALES));
        attackBoxOffseX = (int) (30 * SCALES);
    }

    public void update(int[][] levelData, Player player) {
        updateBehavior(levelData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x - attackBoxOffseX;
        attackBox.y = hitBox.y;
    }

    private void updateBehavior(int[][] levelData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(levelData);
        }
        if (inAir) {
            updateInAir(levelData);
        } else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(levelData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    moved(levelData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }
                    if (aniIndex == 3 && !attackChecked) {
                        checkPlayerHit(attackBox, player);
                    }
                case HIT:
                    break;
            }
        }
    }

    // public void drawAttackBox(Graphics g, int xLevelOffset) {
    //     g.setColor(Color.GREEN);
    //     g.drawRect((int) attackBox.x - xLevelOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    // }

    public int flipX() {
        if (walkDir == RIGHT) {
            return width;
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        } else {
            return 1;
        }
    }

}
