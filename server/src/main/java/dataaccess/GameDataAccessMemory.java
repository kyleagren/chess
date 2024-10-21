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
        GameData gameRepresentation = new GameData(currentSize + 1, "", "", gameName, game);
        games.add(gameRepresentation);
        return (currentSize + 1);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            return games.get(gameID - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new DataAccessException("game does not exist");
        }
    }

    @Override
    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException {
        GameData game = null;
        try {
            game = getGame(gameID - 1);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (playerColor.equals("WHITE")) {
            GameData newGame;
            if (!game.blackUsername().isEmpty()) {
                newGame = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
            }
            else {
                newGame = new GameData(gameID, username, "", game.gameName(), game.game());
            }
            games.set(gameID, newGame);
        }
        else if (playerColor.equals("BLACK")) {
            GameData newGame;
            if (!game.whiteUsername().isEmpty()) {
                newGame = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
            }
            else {
                newGame = new GameData(gameID, "", username, game.gameName(), game.game());
            }
            games.set(gameID, newGame);
        }
    }

    @Override
    public boolean checkAvailability(int gameID, String playerColor) throws DataAccessException {
        GameData game = null;
        try {
            game = getGame(gameID - 1);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (playerColor.equals("WHITE")) {
            return game.whiteUsername().isEmpty();
        }
        else if (playerColor.equals("BLACK")) {
            return game.blackUsername().isEmpty();
        }
        throw new DataAccessException("invalid color provided");
    }

    @Override
    public ArrayList<GameData> getGames() {
        return games;
    }

    @Override
    public void deleteAll() {
        games.clear();
    }
}
