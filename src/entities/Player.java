package entities;

import static main.Game.SCALES;
import static main.Game.TILES_SIZE;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.StatusBar.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.PlayerAudio;
import gamestates.Playing;
import main.Game;
import utilz.Constants;
import utilz.LoadSave;

public class Player extends Entity {
	private BufferedImage[][] animations;

	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] levelData;
	private int flipX = 0;
	private int flipW = 1;
	private boolean attackCheck;
	// Jumping / Gravity
	private float jumpSpeed = -2.75f * Game.SCALES;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALES;
	// Status bas
	private BufferedImage statusbas;
	private int healthWidth = HEALTH_BAR_WIDTH;
	// AttackBox
	private Playing playing;
	private int tileY = 0;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		walkSpeed = 0.79f * SCALES;
		this.currentHealth = maxHealth;
		loadAnimations();
		initHitbox(WIDTH_HITBOX_DEFAULT, HEIGHT_HITBOX_DEFAULT);
		initAttackBox();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitBox.x = x;
		hitBox.y = y;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * SCALES), (int) (20 * SCALES));
	}

	public void update() {
		updateHealthBar();
		if (currentHealth <= 0) {
			if (state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getPlayerAudio().playEffect(PlayerAudio.DIE);
			} else if (aniIndex == getSpriteAmount(DEAD) - 1 && aniTick >= ANISPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getPlayerAudio().stopSong();
				playing.getGame().getPlayerAudio().playEffect(PlayerAudio.GAMEOVER);
			} else {
				updateAnimationTick();
			}
			return;
		}

		updateAttackBox();
		updatePos();
		if (moving) {
			checkPotionTouched();
			checkSpikesTouched();
			tileY = (int) (hitBox.y / TILES_SIZE);
		}
		if (attacking) {
			checkAttack(this);
		}
		updateAnimationTick();
		setAnimation();
	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitBox);
	}

	private void checkAttack(Player player) {
		if (attackCheck || aniIndex != 1)
			return;
		attackCheck = true;
		playing.checkEnemyHit(attackBox);
		playing.checkObjecthitbox(attackBox);
		playing.getGame().getPlayerAudio().playAttackSound();;
	}

	private void updateAttackBox() {
		if (right) {
			attackBox.x = hitBox.x + hitBox.width + (int) SCALES * 2;
		} else if (left) {
			attackBox.x = hitBox.x - hitBox.width - (int) SCALES * 2;
		}
		attackBox.y = hitBox.y + (int) SCALES * 10;
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * HEALTH_BAR_WIDTH);
	}

	public void render(Graphics g, int levelOffset) {
		drawUI(g);
		g.drawImage(animations[state][aniIndex],
				(int) (hitBox.x - X_DRAW_OFFSET) - levelOffset + flipX,
				(int) (hitBox.y - Y_DRAW_OFFSET),
				width * flipW, height, null);
		drawHitbox(g, levelOffset);
		drawAttackBox(g, levelOffset);

	}

	private void drawUI(Graphics g) {
		g.drawImage(statusbas, STATUS_BAS_X, STATUS_BAS_Y, STATUS_BAS_WIDTH, STATUS_BAS_HEIGHT, null);
		g.setColor(Color.RED);
		g.fillRect(HEALTH_BAR_X_START + STATUS_BAS_X, HEALTH_BAR_Y_START + STATUS_BAS_Y, healthWidth,
				HEALTH_BAR_HEIGHT);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= Constants.PlayerConstants.getSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackCheck = false;
			}
		}
	}

	public void changeHealth(int value) {
		currentHealth += value;
		if (currentHealth <= 0) {
			currentHealth = 0;
			// gameOver();
		} else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
		}
	}

	public void changePower(int value) {
		System.out.println("add power success!");
	}

	private void setAnimation() {
		int startAni = state;
		if (moving)
			state = RUNNING;
		else
			state = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		}

		if (attacking) {
			state = ATTACK;
			if (startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}

		if (startAni != state)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		if (!inAir)
			if ((!left && !right) || (right && left))
				return;

		float xSpeed = 0;

		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (!inAir)
			if (!IsEntityOnFloor(hitBox, levelData))
				inAir = true;

		if (inAir) {
			if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, levelData)) {
				hitBox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		} else
			updateXPos(xSpeed);
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		playing.getGame().getPlayerAudio().playEffect(PlayerAudio.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, levelData)) {
			hitBox.x += xSpeed;
		} else {
			hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
		}
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[7][8];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

		statusbas = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAS);
	}

	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
		if (!IsEntityOnFloor(hitBox, levelData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		flipW = 1;
		flipX = 0;

	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		jump = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;

		hitBox.x = x;
		hitBox.y = y;
		if (!IsEntityOnFloor(hitBox, levelData))
			inAir = true;
	}

	public void kill() {
		currentHealth = 0;
	}

	public int getTileY() {
		return tileY;
	}

}