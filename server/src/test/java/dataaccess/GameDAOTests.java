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
            Connection conn = DatabaseManager.getConnection();
            DatabaseManager.deleteDatabase();
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
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(null));
    }

    @Test
    @Order(3)
    public void getGameSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(1));
    }

    @Test
    @Order(4)
    public void getGameFailure() {
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
        Assertions.assertEquals(3, items.size());
    }

    @Test
    @Order(10)
    public void getGamesEmpty() throws DataAccessException {
        gameDAO.deleteAll();

        ArrayList<GameData> items = gameDAO.getGames();
        Assertions.assertEquals(0, items.size());
    }

    @Test
    @Order(11)
    public void deleteAllSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.deleteAll());
    }
}