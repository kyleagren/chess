package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataAccess {
    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void joinGame(int gameID, String playerColor, String username) throws DataAccessException;

    default boolean checkAvailability(int gameID, String playerColor) throws DataAccessException {
        GameData game;
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

    ArrayList<GameData> getGames() throws DataAccessException;

    void deleteAll() throws DataAccessException;
}
