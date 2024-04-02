package gamestates;

import java.awt.event.MouseEvent;

import audio.PlayerAudio;
import main.Game;
import ui.MenuButtons;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButtons buttons) {
        return buttons.getBound().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

    public void setGameState(GameState state) {
        switch (state) {
            case MENU -> game.getPlayerAudio().playSong(PlayerAudio.MENU_1);
            case PLAYING -> game.getPlayerAudio().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }
        GameState.state = state;
    }
}
