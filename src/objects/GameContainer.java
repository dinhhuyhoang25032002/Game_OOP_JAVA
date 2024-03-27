package objects;

import static main.Game.SCALES;
import static utilz.Constants.ObjectConstants.*;

public class GameContainer extends GameObject {

    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }

    private void createHitbox() {
        if (objectType == BOX) {
            initHitbox(25, 18);
            xDrawOffset = (int) (7 * SCALES);
            yDrawOffset = (int) (12 * SCALES);
        } else {
            initHitbox(23, 25);
            xDrawOffset = (int) (8 * SCALES);
            yDrawOffset = (int) (5 * SCALES);
        }
        hitBox.y += yDrawOffset + (int) (2 * SCALES);
        hitBox.x += xDrawOffset / 2;
    }

    public void update() {
        if (doAnimation) {
            updateAnimationTick();
        }
    }
}
