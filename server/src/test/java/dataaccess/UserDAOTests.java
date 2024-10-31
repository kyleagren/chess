package dataaccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAOTests {
    Connection conn;
    UserDataAccess userDAO = new UserDataAccessMySQL();

    @BeforeAll
    public static void init() {
        try {
            DatabaseManager.deleteDatabase();
            Connection conn = DatabaseManager.getConnection();
            DatabaseManager.createDatabase();
            conn.close();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getUserSuccess() {

    }

    @Test
    public void getUserFailure() {

    }

    @Test
    public void createUserSuccess() {

    }

    @Test
    public void createUserFailure() {

    }

    @Test
    public void deleteAllSuccess() {

    }
}
