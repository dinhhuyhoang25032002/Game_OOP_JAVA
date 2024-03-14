package ui;

import gamestates.GameState;
import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class MenuButtons {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH_DEFAULT / 2;
    private GameState state;
    private BufferedImage[] image;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public MenuButtons(int xPos, int yPos, int rowIndex, GameState state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImage();
        initBounds();
    }

    private void initBounds() {
       
            bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
      
    }

    private void loadImage() {

        image = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BUTTON_MENU);
        for (int i = 0; i < image.length; i++) {
            image[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT,
                    B_HEIGHT_DEFAULT);
        }

    }

    public void draw(Graphics g) {
        g.drawImage(image[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean getMouseOver() {
        return mouseOver;
    }

    public boolean getMousePressed() {
        return mousePressed;
    }

    public void resetMouse() {
        this.mouseOver = false;
        this.mousePressed = false;
    }

    public Rectangle getBound() {
        return this.bounds;
    }

    public void applyGameState() {
        GameState.state = state;
    }
}
