package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAOTests {
    Connection conn;
    UserDataAccess userDAO = new UserDataAccessMySQL();

    String username = "";
    String password = "";
    String email = "";

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

    @AfterAll
    public static void cleanUp() {
        try {
            DatabaseManager.deleteDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    public void createUserSuccess() {
        UserData data = new UserData(username, password, email);
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(data));
    }

    @Test
    @Order(2)
    public void createUserFailure() {
        UserData data = new UserData(username, password, email);
        Assertions.assertThrows(Exception.class, () -> userDAO.createUser(data));
    }

    @Test
    @Order(3)
    public void getUserSuccess() {
        Assertions.assertDoesNotThrow(() -> userDAO.getUser(username));
    }

    @Test
    @Order(4)
    public void getUserFailure() throws DataAccessException {
        Assertions.assertNull(userDAO.getUser("meaninglessness"));
    }

    @Test
    @Order(5)
    public void deleteAllSuccess() {
        Assertions.assertDoesNotThrow(() -> userDAO.deleteAll());
    }
}
