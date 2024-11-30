package client;

import chess.ChessGame;
import model.GameData;

public abstract class ChessClient {
    private String token;
    private GameData game;

    public String getToken() {
        return token;
    }

    public void setToken(String newToken) {
        token = newToken;
    }

    public GameData getGame() {
        return game;
    }

    public void setGame(GameData newGame) {
        game = newGame;
    }

    abstract public String eval(String input);
    abstract public String help();
}
