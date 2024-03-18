package entities;

import static main.Game.SCALES;
import static utilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy {

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * SCALES), (int) (19 * SCALES));
    }

}
