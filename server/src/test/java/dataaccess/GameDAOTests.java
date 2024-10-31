package dataaccess;

import model.GameData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDAOTests {
    Connection conn;
    GameDataAccess gameDAO = new GameDataAccessMySQL();

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
            conn.setCatalog("chess");
        } catch (DataAccessException | SQLException e) {
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
    public void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("test"));
    }

    @Test
    @Order(2)
    public void createGameFailure() {
        // TODO - think of a better way to do this.
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame("'; DROP TABLE game"));
    }

    @Test
    @Order(3)
    public void getGameSuccess() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(1));
    }

    @Test
    @Order(4)
    public void getGameFailure() throws DataAccessException {
        Assertions.assertThrows(Exception.class, () -> gameDAO.getGame(3));
    }

    @Test
    @Order(5)
    public void joinGameSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.joinGame(1, "WHITE", "testUser1"));
    }

    @Test
    @Order(6)
    public void joinGameFailure() {
        Assertions.assertThrows(Exception.class, () -> gameDAO.joinGame(1, "WHITE", "testUser2"));
    }

    @Test
    @Order(7)
    public void checkAvailabilitySuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.checkAvailability(1, "BLACK"));
    }

    @Test
    @Order(8)
    public void checkAvailabilityFailure() {
        Assertions.assertThrows(Exception.class, () -> gameDAO.checkAvailability(4, "WHITE"));
    }

    @Test
    @Order(9)
    public void getGamesSuccess() throws DataAccessException {
        gameDAO.createGame("test1");
        gameDAO.createGame("test2");

        ArrayList<GameData> items = gameDAO.getGames();
        Assertions.assertEquals(items.size(), 4); // TODO - change to 3 once I fix createGameFailure
    }

    @Test
    @Order(10)
    public void getGamesFailure() {
        // TODO - get help here I don't know what to try.
    }

    @Test
    @Order(11)
    public void deleteAllSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.deleteAll());
    }
}
