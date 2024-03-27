package objects;

import static main.Game.SCALES;

public class Spike extends GameObject {

    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int) (16 * SCALES);
        hitBox.y += yDrawOffset;
    }

}
