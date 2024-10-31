package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class AuthDataAccessMySQL implements AuthDataAccess {
    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){

            String authToken;
            String username;

            String sql = "SELECT token, username FROM auth WHERE token=?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, token);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        authToken = rs.getString("token");
                        username = rs.getString("username");
                        return new AuthData(authToken, username);
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            String sql = "INSERT INTO auth (token, username) VALUES(?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, data.authToken());
                preparedStatement.setString(2, data.username());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        try {
            UUID.fromString(token);
        } catch (Exception e) {
            throw new DataAccessException("doesn't exist");
        }
        try (Connection conn = DatabaseManager.getConnection()){
            String sql = "DELETE FROM auth WHERE token=?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, token);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("TRUNCATE auth")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
