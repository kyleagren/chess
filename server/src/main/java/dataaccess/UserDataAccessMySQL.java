package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataAccessMySQL implements UserDataAccess {
    @Override
    public UserData getUser(String username) {
        boolean found = false;

        String usernameResult = "";
        String password = "";
        String email = "";

        Connection conn;

        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
        return null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        Connection conn;
        String hashedPassword = hashPassword(userData.password());
        if (getUser(userData.username()) == null) {
            try {
                conn = DatabaseManager.getConnection();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
            String sql = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userData.email());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new DataAccessException("User already exists");
        }
    }

    @Override
    public void deleteAll() {
        Connection conn;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var preparedStatement = conn.prepareStatement("TRUNCATE user")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
