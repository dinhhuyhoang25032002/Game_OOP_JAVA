package objects;

import static main.Game.SCALES;

import main.Game;

public class Potion extends GameObject {
    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * SCALES);
        yDrawOffset = (int) (2 * SCALES);
        maxHoverOffset = (int) (10 * SCALES);
    }

    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.075 * SCALES * hoverDir);
        if (hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        } else if (hoverOffset < 0) {
            hoverDir = 1;
        }
        hitBox.y = y + hoverOffset;
    }
}
