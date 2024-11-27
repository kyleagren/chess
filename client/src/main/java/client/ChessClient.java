package client;

import chess.ChessGame;

public abstract class ChessClient {
    private String token;
    private ChessGame game;

    public String getToken() {
        return token;
    }

    public void setToken(String newToken) {
        token = newToken;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame newGame) {
        game = newGame;
    }

    abstract public String eval(String input);
    abstract public String help();
}
