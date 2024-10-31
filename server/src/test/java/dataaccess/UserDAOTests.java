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
    public static void init() throws DataAccessException, SQLException {
        DatabaseManager.deleteDatabase();
        Connection conn = DatabaseManager.getConnection();
        DatabaseManager.createDatabase();
        conn.close();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.close();
    }

    @AfterAll
    public static void cleanUp() throws DataAccessException {
        DatabaseManager.deleteDatabase();
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
