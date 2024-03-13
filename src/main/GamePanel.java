package main;

import javax.swing.JPanel;

import inputs.KeybroadInputs;

import inputs.MouseListenerInputs;

import java.awt.Dimension;

import java.awt.Graphics;

import static main.Game.GAME_WIDTH;

import static main.Game.GAME_HEIGHT;;

public class GamePanel extends JPanel {

    private MouseListenerInputs mouseListenerInputs;
    private Game game;

    public GamePanel(Game game) {
        mouseListenerInputs = new MouseListenerInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new KeybroadInputs(this));
        addMouseListener(mouseListenerInputs);
        addMouseMotionListener(mouseListenerInputs);

        setFocusable(true);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        // setMinimumSize(size);
        setPreferredSize(size);
        // setMaximumSize(size);

    }

    public void updateGame() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
