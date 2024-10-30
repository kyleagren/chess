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
        Connection conn;

        int id = 0;
        String gameName = "";
        String gameString = "";
        String whiteUsername = null;
        String blackUsername = null;

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT (id, gameName, game, whiteUsername, blackUsername) FROM game WHERE id=?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    id = rs.getInt("id");
                    gameName = rs.getString("gameName");
                    gameString = rs.getString("game");
                    whiteUsername = rs.getString("whiteUsername");
                    blackUsername = rs.getString("blackUsername");
                }
                ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
                return new GameData(id, whiteUsername, blackUsername, gameName, game);
            }
        } catch (SQLException e) {
            throw new DataAccessException("game does not exist");
        }
    }

    @Override
    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException {
        Connection conn;

        String whiteUsername = null;
        String blackUsername = null;

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT (whiteUsername, blackUsername) FROM game WHERE id=?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    whiteUsername = rs.getString("whiteUsername");
                    blackUsername = rs.getString("blackUsername");
                }
            }
            if (playerColor.equals("WHITE")) {
                if (whiteUsername == null) {
                    try (var newStatement = conn.prepareStatement("UPDATE game SET whiteUsername=? WHERE id=?")) {
                        newStatement.setString(1, username);
                        newStatement.setInt(2, gameID);

                        newStatement.executeUpdate();
                    }
                }
            }
            if (playerColor.equals("BLACK")) {
                if (blackUsername == null) {
                    try (var newStatement = conn.prepareStatement("UPDATE game SET blackUsername=? WHERE id=?")) {
                        newStatement.setString(1, username);
                        newStatement.setInt(2, gameID);

                        newStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean checkAvailability(int gameID, String playerColor) throws DataAccessException {
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

    @Override
    public ArrayList<GameData> getGames() throws DataAccessException {
        Connection conn;
        ArrayList<GameData> games = new ArrayList<>();

        int id = 0;
        String gameName = "";
        String gameString = "";
        String whiteUsername = null;
        String blackUsername = null;

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT (id, gameName, game, whiteUsername, blackUsername) FROM game";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    id = rs.getInt("id");
                    gameName = rs.getString("gameName");
                    gameString = rs.getString("game");
                    whiteUsername = rs.getString("whiteUsername");
                    blackUsername = rs.getString("blackUsername");
                }
                ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
                games.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
            }
        } catch (SQLException e) {
            throw new DataAccessException("game does not exist");
        }
        return games;
    }

    @Override
    public void deleteAll() {

    }
}
