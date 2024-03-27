package utilz;

import static main.Game.TILES_SIZE;
import static utilz.Constants.EnemyConstants.CRABBY;
import static utilz.Constants.ObjectConstants.BARREL;
import static utilz.Constants.ObjectConstants.BLUE_POTION;
import static utilz.Constants.ObjectConstants.BOX;
import static utilz.Constants.ObjectConstants.RED_POTION;
import static utilz.Constants.ObjectConstants.SPIKE;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

public class HelpMethods {

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
		if (!IsSolid(x, y, levelData))
			if (!IsSolid(x + width, y + height, levelData))
				if (!IsSolid(x + width, y, levelData))
					if (!IsSolid(x, y + height, levelData))
						return true;
		return false;
	}

	private static boolean IsSolid(float x, float y, int[][] levelData) {

		int maxWidth = levelData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, levelData);
	}

	public static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {
		int value = levelData[yTile][xTile];
		if (value >= 48 || value < 0 || value != 11)
			return true;
		return false;
	}

	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
		// Check the pixel below bottomleft and bottomright
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData))
				return false;
		return true;

	}

	public static boolean isFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] levelData) {
		if (xSpeed > 0) {
			return IsSolid(hitBox.x + xSpeed + hitBox.width, hitBox.y + hitBox.height + 1, levelData);
		}
		return IsSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, levelData);
	}

	public static boolean isAllTileWalkable(int startX, int endX, int y, int[][] levelData) {
		for (int i = 0; i < endX - startX; i++) {
			if (IsTileSolid(startX + i, y, levelData)) {
				return false;
			}
			if (!IsTileSolid(startX + i, y + 1, levelData)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSightClear(int[][] levelData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox,
			int tileY) {
		int firstXTile = (int) (firstHitbox.x / TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / TILES_SIZE);
		if (firstXTile > secondXTile) {
			return isAllTileWalkable(secondXTile, firstXTile, tileY, levelData);
		} else {
			return isAllTileWalkable(firstXTile, secondXTile, tileY, levelData);
		}
	}

	public static int[][] GetLevelData(BufferedImage img) {
		int[][] levelData = new int[img.getHeight()][img.getWidth()];
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int value = (int) color.getRed();
				if (value >= 48) {
					value = 0;
				}
				levelData[i][j] = value;
			}
		}
		return levelData;
	}

	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getGreen();
				if (value == 100) {
					return new Point(j * TILES_SIZE, i * TILES_SIZE);
				}
			}
		}
		return new Point(2 * TILES_SIZE, 3 * TILES_SIZE);
	}

	public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
		ArrayList<Crabby> list = new ArrayList<>();
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getGreen();
				if (value == CRABBY) {
					list.add(new Crabby(j * TILES_SIZE, i * TILES_SIZE));
				}
			}
		}
		return list;
	}

	public static ArrayList<Potion> GetPotion(BufferedImage img) {
		ArrayList<Potion> list = new ArrayList<>();
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getBlue();
				if (value == RED_POTION || value == BLUE_POTION) {
					list.add(new Potion(j * TILES_SIZE, i * TILES_SIZE, value));
				}
			}
		}
		return list;
	}

	public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
		ArrayList<GameContainer> list = new ArrayList<>();
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getBlue();
				if (value == BARREL || value == BOX) {
					list.add(new GameContainer(j * TILES_SIZE, i * TILES_SIZE, value));
				}
			}
		}
		return list;
	}

	public static ArrayList<Spike> GetSpikes(BufferedImage img) {
		ArrayList<Spike> list = new ArrayList<>();
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getBlue();
				if (value == SPIKE) {
					list.add(new Spike(j * TILES_SIZE, i * TILES_SIZE, SPIKE));
				}
			}
		}
		return list;
	}
}