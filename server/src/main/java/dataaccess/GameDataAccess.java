package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDataAccess {
    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void joinGame(int gameID, String playerColor, String username) throws DataAccessException;

    boolean checkAvailability(int gameID, String playerColor) throws DataAccessException;

    ArrayList<GameData> getGames() throws DataAccessException;

    void deleteAll() throws DataAccessException;
}
