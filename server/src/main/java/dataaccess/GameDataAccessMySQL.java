package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class GameDataAccessMySQL implements GameDataAccess {
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException {

    }

    @Override
    public boolean checkAvailability(int gameID, String playerColor) throws DataAccessException {
        return false;
    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
