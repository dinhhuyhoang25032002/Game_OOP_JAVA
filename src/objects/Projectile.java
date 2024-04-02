package objects;

import java.awt.geom.Rectangle2D;

import static main.Game.SCALES;
import static utilz.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitBox;
    private int dir;
    private boolean active = true;

    public Projectile(int x, int y, int dir) {
        int xOffset = (int) (-3 * SCALES);
        int yOffset = (int) (5 * SCALES);
        if (dir == 1) {
            xOffset = (int) (29 * SCALES);
        }
        hitBox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    public void updatePos() {
        hitBox.x += dir * SPEED;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPos(int x, int y) {
        hitBox.x = x;
        hitBox.y = y;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

}
