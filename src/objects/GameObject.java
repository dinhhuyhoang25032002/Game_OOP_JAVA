package objects;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import static main.Game.SCALES;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.*;
import java.awt.Graphics;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void initHitbox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * SCALES), (int) (SCALES * height));
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANISPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objectType)) {
                aniIndex = 0;
                if (objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    active = false;
                }

            }
        }
    }

    public void reset() {
        aniIndex = 0;
        aniTick = 0;
        active = true;
        
        if (objectType == BARREL || objectType == BOX)
            doAnimation = false;
        else
            doAnimation = true;
    }

    public void drawHitbox(Graphics g, int xLevelOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle2D.Float hitBox) {
        this.hitBox = hitBox;
    }

    public boolean isDoAnimation() {
        return doAnimation;
    }

    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex() {
        return aniIndex;
    }

}
