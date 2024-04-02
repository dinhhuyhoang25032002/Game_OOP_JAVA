package objects;

import static main.Game.SCALES;
import static main.Game.TILES_SIZE;

public class Cannon extends GameObject {
    private int tileY;

    public Cannon(int x, int y, int objectType) {
        super(x, y, objectType);
        tileY = y / TILES_SIZE;
        initHitbox(40, 26);
        hitBox.x -= (int) (4 * SCALES);
        hitBox.y += (int) (6 * SCALES);
    }

    public void update() {
        if (doAnimation) {
            updateAnimationTick();
        }
    }

    public int getTileY() {
        return tileY;
    }
    
}
