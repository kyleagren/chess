package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDataAccessMySQL implements UserDataAccess {
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            boolean found = false;

            String usernameResult = "";
            String password = "";
            String email = "";

            String sql = "SELECT id, username, password, email FROM user WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        usernameResult = rs.getString("username");
                        password = rs.getString("password");
                        email = rs.getString("email");
                        found = true;
                    }
                    if (found) {
                        return new UserData(usernameResult, password, email);
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
    public void createUser(UserData userData) throws DataAccessException {
        if (getUser(userData.username()) == null) {
            try (Connection conn = DatabaseManager.getConnection()){
                String sql = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
                try (var preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setString(1, userData.username());
                    preparedStatement.setString(2, userData.password());
                    preparedStatement.setString(3, userData.email());

                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage());
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        else {
            throw new DataAccessException("User already exists");
        }
    }

    @Override
    public void deleteAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("TRUNCATE user")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
