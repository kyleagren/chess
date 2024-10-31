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
        try (Connection conn = DatabaseManager.getConnection()){
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
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        int id;
        String gameName;
        String gameString;
        String whiteUsername;
        String blackUsername;

        try (Connection conn = DatabaseManager.getConnection()){
            String sql = "SELECT id, gameName, game, whiteUsername, blackUsername FROM game WHERE id=?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt("id");
                        gameName = rs.getString("gameName");
                        gameString = rs.getString("game");
                        whiteUsername = rs.getString("whiteUsername");
                        blackUsername = rs.getString("blackUsername");

                        ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
                        return new GameData(id, whiteUsername, blackUsername, gameName, game);
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("game does not exist");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        throw new DataAccessException("invalid index");
    }

    @Override
    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException {
        String whiteUsername = null;
        String blackUsername = null;
        if (gameID == 0) {
            throw new DataAccessException("invalid game ID");
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT whiteUsername, blackUsername FROM game WHERE id=?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        whiteUsername = rs.getString("whiteUsername");
                        blackUsername = rs.getString("blackUsername");
                    }
                }
                if (playerColor.equals("WHITE") && whiteUsername == null) {
                    try (var newStatement = conn.prepareStatement("UPDATE game SET whiteUsername=? WHERE id=?")) {
                        newStatement.setString(1, username);
                        newStatement.setInt(2, gameID);

                        newStatement.executeUpdate();
                    }
                }
                else if (playerColor.equals("BLACK") && blackUsername == null) {
                    try (var newStatement = conn.prepareStatement("UPDATE game SET blackUsername=? WHERE id=?")) {
                        newStatement.setString(1, username);
                        newStatement.setInt(2, gameID);

                        newStatement.executeUpdate();
                    }
                }
                else {
                    throw new DataAccessException("username already taken");
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();

        int id;
        String gameName;
        String gameString;
        String whiteUsername;
        String blackUsername;

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT id, gameName, game, whiteUsername, blackUsername FROM game";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        id = rs.getInt("id");
                        gameName = rs.getString("gameName");
                        gameString = rs.getString("game");
                        whiteUsername = rs.getString("whiteUsername");
                        blackUsername = rs.getString("blackUsername");

                        ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
                        games.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("game does not exist");
            }
            return games;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("TRUNCATE game")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
