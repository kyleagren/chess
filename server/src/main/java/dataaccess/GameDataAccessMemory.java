package dataaccess;

import chess.ChessGame;

import java.util.ArrayList;

public class GameDataAccessMemory implements GameDataAccess {
    private static ArrayList<ChessGame> games = new ArrayList<>();

    @Override
    public int createGame(String gameName) {
        ChessGame game = new ChessGame();
        games.add(game);
        return (games.indexOf(game));
    }
}
