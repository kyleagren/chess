package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class GameDataAccessMemory implements GameDataAccess {
    private static ArrayList<GameData> games = new ArrayList<>();

    @Override
    public int createGame(String gameName) {
        int currentSize = games.size();
        ChessGame game = new ChessGame();
        GameData gameRepresentation = new GameData(currentSize, null, null, gameName, game);
        games.add(gameRepresentation);
        return (currentSize);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            return games.get(gameID);
        } catch (IndexOutOfBoundsException e) {
            throw new DataAccessException("game does not exist");
        }
    }


    @Override
    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException {
        GameData game = null;
        try {
            game = getGame(gameID);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (playerColor.equals("WHITE")) {
            GameData newGame;
            if (game.blackUsername() != null) {
                newGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
            }
            else {
                newGame = new GameData(gameID, username, null, game.gameName(), game.game());
            }
            games.set(gameID, newGame);
        }
        else if (playerColor.equals("BLACK")) {
            GameData newGame;
            if (game.whiteUsername() != null) {
                newGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
            }
            else {
                newGame = new GameData(gameID, null, username, game.gameName(), game.game());
            }
            games.set(gameID, newGame);
        }
    }

    @Override
    public boolean checkAvailability(int gameID, String playerColor) throws DataAccessException {
        GameData game = null;
        try {
            game = getGame(gameID);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (playerColor.equals("WHITE")) {
            return game.whiteUsername() == null;
        }
        else if (playerColor.equals("BLACK")) {
            return game.blackUsername() == null;
        }
        throw new DataAccessException("invalid color provided");
    }
}
