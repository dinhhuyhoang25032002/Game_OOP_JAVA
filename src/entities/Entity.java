package entities;

import static main.Game.SCALES;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed = 0f;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    protected void initHitbox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * SCALES), (int) (SCALES * height));
    }

    protected void drawHitbox(Graphics g, int xLevelOffset) {

        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    protected void drawAttackBox(Graphics g, int xLevelOffset) {
        g.setColor(Color.GREEN);
        g.drawRect((int) attackBox.x - xLevelOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public Rectangle2D.Float getHitbox() {
        return hitBox;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
    
}
