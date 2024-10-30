package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthDataAccessMySQL implements AuthDataAccess {
    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        Connection conn;

        boolean found = false;

        String authToken = "";
        String username = "";

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT (token, username) FROM auth WHERE token=?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, token);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    authToken = rs.getString("token");
                    username = rs.getString("username");
                    found = true;
                }
                if (found) {
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        Connection conn;

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String sql = "INSERT INTO auth (token, username) VALUES(?, ?)";
        try (var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, data.authToken());
            preparedStatement.setString(2, data.username());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        Connection conn;

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String sql = "DELETE FROM auth WHERE token=?";
        try (var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Not found");
        }
    }

    @Override
    public void deleteAll() throws DataAccessException {
        Connection conn;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var preparedStatement = conn.prepareStatement("TRUNCATE auth")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
