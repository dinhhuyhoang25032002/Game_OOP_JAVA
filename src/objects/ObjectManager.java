package objects;

import static main.Game.TILES_SIZE;
import static utilz.Constants.ObjectConstants.*;

import static utilz.Constants.Projectiles.CANNON_BALL_HEIGHT;
import static utilz.Constants.Projectiles.CANNON_BALL_WIDTH;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHitting;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.PlayerAudio;
import entities.Player;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage spikeImgs, projectileImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();;
    private int attackDistance = TILES_SIZE;

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
        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImgs.length; i++) {
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);
        }
        projectileImg = LoadSave.GetSpriteAtlas(LoadSave.BALL_CANNON);

    }

    public void update(int[][] levelData, Player player) {
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
        updateCannons(levelData, player);
        updateProjectiles(levelData, player);
    }

    private void updateProjectiles(int[][] levelData, Player player) {
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).isActive()) {
                projectiles.get(i).updatePos();
                if (projectiles.get(i).getHitBox().intersects(player.getHitbox())) {
                    player.changeHealth(-5);
                    projectiles.get(i).setActive(false);
                } else if (IsProjectileHitting(projectiles.get(i), levelData)) {
                    projectiles.get(i).setActive(false);
                }
            }
        }
    }

    private void updateCannons(int[][] levelData, Player player) {
        for (int i = 0; i < cannons.size(); i++) {
            if (cannons.get(i).getTileY() == player.getTileY()) {
                if (isPlayerInRange(cannons.get(i), player)) {
                    if (isPlayerInfontOfCannon(cannons.get(i), player)) {
                        if (CanCannonSeePlayer(levelData, player.getHitbox(), cannons.get(i).getHitBox(),
                                cannons.get(i).getTileY())) {
                            cannons.get(i).setDoAnimation(true);
                        }
                    }
                }
            }
            cannons.get(i).update();
             if (cannons.get(i).getAniIndex() == 4 && cannons.get(i).getAniTick() == 0) {
                 shootCannon(cannons.get(i));
                playing.getGame().getPlayerAudio().playEffect(PlayerAudio.CANNON);
            }
        }
    }

    private void shootCannon(Cannon cannon) {
        int dir = 1;
        if (cannon.getObjectType() == CANNON_LEFT) {
            dir = -1;
        }
        projectiles.add(new Projectile((int) (cannon.getHitBox().x), (int) (cannon.getHitBox().y), dir));
    }

    private boolean isPlayerInfontOfCannon(Cannon cannon, Player player) {
        if (cannon.getObjectType() == CANNON_LEFT) {
            if (cannon.getHitBox().x > player.getHitbox().x) {
                return true;
            }
        } else if (cannon.getObjectType() == CANNON_RIGHT) {
            if (cannon.getHitBox().x < player.getHitbox().x) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon cannon, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - cannon.getHitBox().x);
        return absValue <= attackDistance * 5;
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
                    playing.getGame().getPlayerAudio().playEffect(PlayerAudio.DESTROY_BOX);
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
        drawCannon(g, xLevelOffset);
        drawProjectiles(g, xLevelOffset);
    }

    private void drawProjectiles(Graphics g, int xLevelOffset) {
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).isActive()) {
                g.drawImage(projectileImg, (int) (projectiles.get(i).getHitBox().x - xLevelOffset),
                        (int) (projectiles.get(i).getHitBox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    private void drawCannon(Graphics g, int xLevelOffset) {
        for (int i = 0; i < cannons.size(); i++) {
            int x = (int) (cannons.get(i).getHitBox().x - xLevelOffset);
            int width = CANNON_WIDTH;

            if (cannons.get(i).getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[cannons.get(i).getAniIndex()],
                    x, (int) (cannons.get(i).getHitBox().y),
                    width, CANNON_HEIGHT, null);
        }
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
        cannons = newLevel.getCannons();
        projectiles.clear();
    }

    public void resetAll() {
        loadObjects(playing.getLevelManager().getCurrLevel());
        for (int i = 0; i < potions.size(); i++) {
            potions.get(i).reset();
        }
        for (GameContainer gc : containers) {
            gc.reset();
        }

        for (Cannon cannon : cannons) {
            cannon.reset();
        }
    }
}
