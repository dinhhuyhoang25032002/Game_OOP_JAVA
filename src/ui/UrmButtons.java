package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.UrmButtons.*;
import utilz.LoadSave;

public class UrmButtons extends PauseButton {
    private BufferedImage[] image;
    private int rowIndex, index;
    private boolean mousePressed, mouseOver;

    public UrmButtons(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadUrmButtons();
    }

    private void loadUrmButtons() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        image = new BufferedImage[3];
        for (int i = 0; i < image.length; i++) {
            image[i] = temp.getSubimage(i * URM_BUTTONS_DEFAUTL_SIZE, URM_BUTTONS_DEFAUTL_SIZE * rowIndex,
                    URM_BUTTONS_DEFAUTL_SIZE,
                    URM_BUTTONS_DEFAUTL_SIZE);
        }
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

    public void draw(Graphics g) {
        g.drawImage(image[index], x, y, URM_BUTTONS_SIZE, URM_BUTTONS_SIZE, null);
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
        
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
}
