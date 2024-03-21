package entities;

import static main.Game.SCALES;
import static main.Game.TILES_SIZE;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Direction.*;
import java.awt.geom.Rectangle2D;

public abstract class Enemy extends Entity {
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir = false;
    protected float fallSpeed, gravity = 0.04f * SCALES;
    protected float walkSpeed = 0.34f * SCALES;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = TILES_SIZE;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] levelData) {
        if (!IsEntityOnFloor(hitBox, levelData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected int getYTilePlayer(Player player) {
        return (int) (player.getHitbox().y / TILES_SIZE);
    }

    protected void updateInAir(int[][] levelData) {
        if (CanMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, levelData)) {
            hitBox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            tileY = (int) (hitBox.y / TILES_SIZE);
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitBox)) {
            player.changeHealth(-GetEnemyDamage(enemyType));
        }
        attackChecked = true;
    }

    protected void moved(int[][] levelData) {
        float xSpeed = 0;
        if (walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, levelData)) {
            if (isFloor(hitBox, xSpeed, levelData)) {
                hitBox.x += xSpeed;
                return;
            }
        }
        changeWalkDir();
    }

    protected boolean canSeePlayer(int[][] levelData, Player player) {
        if (getYTilePlayer(player) == tileY) {
            if (isPlayerInRange(player)) {
                if (isSightClear(levelData, hitBox, player.hitBox, tileY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            newState(DEAD);
        } else {
            newState(HIT);
        }
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitBox.x > hitBox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);

        if (getYTilePlayer(player) == tileY) {
            return absValue <= attackDistance;
        }
        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5;
    }

    protected void newState(int enemyState) {
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
                // switch (enemyState) {
                // case ATTACK|HIT:
                // enemyState = IDLE;
                // case DEAD:
                // active = false;
                // default:
                // break;
                // }
                switch (enemyState) {
                    case ATTACK, HIT -> enemyState = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        fallSpeed = 0;
    }
}
