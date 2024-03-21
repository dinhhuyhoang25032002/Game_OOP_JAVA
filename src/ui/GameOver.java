package ui;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gamestates.GameState;
import gamestates.Playing;

public class GameOver {
    private Playing playing;

    public GameOver(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Over", GAME_WIDTH / 2, GAME_HEIGHT / 3);
        g.drawString("Press esc to enter Main Menu!", GAME_WIDTH / 2, GAME_HEIGHT / 2);

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }
}
