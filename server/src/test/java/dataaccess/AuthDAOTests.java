package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDAOTests {
    Connection conn;
    AuthDataAccess authDAO = new AuthDataAccessMySQL();
    String goodToken = "";
    String username = "testUser1";

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
    public void createAuthSuccess() {
        goodToken = UUID.randomUUID().toString();
        AuthData data = new AuthData(goodToken, username);
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(data));
    }

    @Test
    @Order(2)
    public void createAuthFailure() {
        // TODO - how do I fail here as well?
    }

    @Test
    @Order(3)
    public void getAuthSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth(goodToken));
    }

    @Test
    @Order(4)
    public void getAuthFailure() {
        Assertions.assertThrows(Exception.class, () -> authDAO.getAuth(UUID.randomUUID().toString()));
    }

    @Test
    @Order(5)
    public void deleteAuthSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(goodToken));
    }

    @Test
    @Order(6)
    public void deleteAuthFailure() {
        Assertions.assertThrows(Exception.class, () -> authDAO.deleteAuth(goodToken));
    }

    @Test
    @Order(7)
    public void deleteAllSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAll());
    }
}
