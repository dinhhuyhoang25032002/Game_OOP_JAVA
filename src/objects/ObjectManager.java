package objects;

import static utilz.Constants.ObjectConstants.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage spikeImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage potionSprites = LoadSave.GetSpriteAtlas(LoadSave.POTION_SPRITES);
        potionImgs = new BufferedImage[2][7];
        for (int i = 0; i < potionImgs.length; i++) {
            for (int j = 0; j < potionImgs[i].length; j++) {
                potionImgs[i][j] = potionSprites.getSubimage(12 * j, 16 * i, 12, 16);
            }
        }
        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_SPRITES);
        containerImgs = new BufferedImage[2][8];
        for (int i = 0; i < potionImgs.length; i++) {
            for (int j = 0; j < potionImgs[i].length; j++) {
                containerImgs[i][j] = containerSprite.getSubimage(40 * j, 30 * i, 40, 30);
            }
        }

        spikeImgs = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
    }

    public void update() {
        for (int i = 0; i < potions.size(); i++) {
            if (potions.get(i).isActive()) {
                potions.get(i).update();
            }
        }
        for (int i = 0; i < containers.size(); i++) {
            if (containers.get(i).isActive()) {
                containers.get(i).update();
            }
        }
    }

    public void checkSpikesTouched(Player player) {
        for (int i = 0; i < spikes.size(); i++) {
            if (spikes.get(i).getHitBox().intersects(player.getHitbox())) {
                player.kill();
            }
        }
    }

    public void checkObjectTouched(Rectangle2D.Float hitBox) {
        for (int i = 0; i < potions.size(); i++) {
            if (potions.get(i).isActive()) {
                if (hitBox.intersects(potions.get(i).getHitBox())) {
                    potions.get(i).setActive(false);
                    applyEffectToPlayer(potions.get(i));
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjectType() == RED_POTION) {
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        } else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        for (int i = 0; i < containers.size(); i++) {
            if (containers.get(i).isActive() && !containers.get(i).doAnimation) {
                if (containers.get(i).getHitBox().intersects(attackBox)) {
                    containers.get(i).setDoAnimation(true);
                    int type = 0;
                    if (containers.get(i).getObjectType() == BARREL) {
                        type = 1;
                    }
                    potions.add(new Potion(
                            (int) (containers.get(i).getHitBox().x + containers.get(i).getHitBox().width / 2),
                            (int) (containers.get(i).getHitBox().y - containers.get(i).getHitBox().height / 2), type));
                    return;

                }
            }
        }
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
        drawTraps(g, xLevelOffset);
    }

    private void drawTraps(Graphics g, int xLevelOffset) {
        for (int i = 0; i < spikes.size(); i++) {
            g.drawImage(spikeImgs, (int) (spikes.get(i).getHitBox().x - xLevelOffset),
                    (int) (spikes.get(i).getHitBox().y - spikes.get(i).getyDrawOffset()), SPIKE_WIDTH,
                    SPIKE_HEIGHT, null);
        }
    }

    private void drawContainers(Graphics g, int xLevelOffset) {
        for (int i = 0; i < containers.size(); i++) {
            if (containers.get(i).isActive()) {
                int type = 0;
                if (containers.get(i).getObjectType() == BARREL)
                    type = 1;

                g.drawImage(containerImgs[type][containers.get(i).getAniIndex()],
                        (int) (containers.get(i).getHitBox().x - containers.get(i).getxDrawOffset() - xLevelOffset),
                        (int) (containers.get(i).getHitBox().y - containers.get(i).getyDrawOffset()),
                        CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }

        }
    }

    private void drawPotions(Graphics g, int xLevelOffset) {
        for (int i = 0; i < potions.size(); i++) {
            if (potions.get(i).isActive()) {
                int type = 0;
                if (potions.get(i).getObjectType() == RED_POTION)
                    type = 1;

                g.drawImage(potionImgs[type][potions.get(i).getAniIndex()],
                        (int) (potions.get(i).getHitBox().x - potions.get(i).getxDrawOffset() - xLevelOffset),
                        (int) (potions.get(i).getHitBox().y - potions.get(i).getyDrawOffset()),
                        POTION_WIDTH, POTION_HEIGHT, null);
            }
        }
    }

    public void loadObjects(Level newLevel) {
        containers = new ArrayList<>(newLevel.getContainers());
        potions = new ArrayList<>(newLevel.getPotions());
        spikes = newLevel.getSpikes();
    }

    public void resetAll() {
        loadObjects(playing.getLevelManager().getCurrLevel());
        for (int i = 0; i < potions.size(); i++) {
            potions.get(i).reset();
        }
        for (GameContainer gc : containers) {
            gc.reset();
        }
    }
}
