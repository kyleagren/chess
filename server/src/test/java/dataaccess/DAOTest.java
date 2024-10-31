package dataaccess;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

public class DAOTest {
    Connection conn;

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
}
