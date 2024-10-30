package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GameDataAccessMySQL implements GameDataAccess {
    @Override
    public int createGame(String gameName) throws DataAccessException {
        Connection conn;

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        ChessGame game = new ChessGame();

        try (var preparedStatement = conn.prepareStatement(
                "INSERT INTO game (gameName, game) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameName);

            String json = new Gson().toJson(game);
            preparedStatement.setString(2, json);

            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();
            var id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
