package utilz;

import static main.Game.SCALES;

import java.security.PublicKey;

import main.Game;

public class Constants {

    public static class EnemyConstants {

        public static final int CRABBY = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;
        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * SCALES);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * SCALES);
        public static final int CRABBY_DRAW_OFFSET_X = (int) (26 * SCALES);
        public static final int CRABBY_DRAW_OFFSET_Y = (int) (9 * SCALES);

        public static final int getSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case CRABBY:
                    switch (enemyState) {
                        case IDLE:
                            return 9;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 4;
                        case DEAD:
                            return 5;
                    }
            }
            return 0;
        }
    }

    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 25;

        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * SCALES);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * SCALES);
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * SCALES);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * SCALES);

    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALES);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALES);
        }

        public static class PauseButton {
            public static final int SOUND_BUTTON_SIZE_DEFAULT = 41;
            public static final int SOUND_BUTTON_SIZE = (int) (SOUND_BUTTON_SIZE_DEFAULT * Game.SCALES);

        }

        public static class UrmButtons {
            public static final int URM_BUTTONS_DEFAUTL_SIZE = 56;
            public static final int URM_BUTTONS_SIZE = (int) (URM_BUTTONS_DEFAUTL_SIZE * SCALES);

        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * SCALES);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * SCALES);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * SCALES);

        }
    }

    public static class Direction {
        public static final int UP = 0;
        public static final int RIGHT = 1;
        public static final int DOWN = 2;
        public static final int LEFT = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;

        public static final int WIDTH_HITBOX_DEFAULT = 20;
        public static final int HEIGHT_HITBOX_DEFAULT = 27;
        public static final int WIDTH_HITBOX = (int) (WIDTH_HITBOX_DEFAULT * SCALES);
        public static final int HEIGHT_HITBOX = (int) (HEIGHT_HITBOX_DEFAULT * SCALES);
        public static final float X_DRAW_OFFSET = 21 * Game.SCALES;
        public static final float Y_DRAW_OFFSET = 4 * Game.SCALES;

        public static int getSpriteAmount(int player_actions) {
            switch (player_actions) {
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMP:
                case ATTACK_1:
                case ATTACK_JUMP_1:
                case ATTACK_JUMP_2:
                    return 3;
                case GROUND:
                    return 2;
                case FALLING:
                default:
                    return 1;
            }
        }

    }
}
