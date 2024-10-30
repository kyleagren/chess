package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

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
    public void createAuth(AuthData data) {

    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void deleteAll() {

    }
}
