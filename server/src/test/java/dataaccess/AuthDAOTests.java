package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;

import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDAOTests extends DAOTest {
    AuthDataAccess authDAO = new AuthDataAccessMySQL();
    String goodToken = UUID.randomUUID().toString();
    String username = "testUser1";

    @Test
    @Order(1)
    public void createAuthSuccess() {
        AuthData data = new AuthData(goodToken, username);
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(data));
    }

    @Test
    @Order(2)
    public void createAuthFailure() {
        AuthData data = new AuthData(null, null);
        Assertions.assertThrows(Exception.class, () -> authDAO.createAuth(data));
    }

    @Test
    @Order(3)
    public void getAuthSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth(goodToken));
    }

    @Test
    @Order(4)
    public void getAuthFailure() throws DataAccessException {
        Assertions.assertNull(authDAO.getAuth(UUID.randomUUID().toString()));
    }

    @Test
    @Order(5)
    public void deleteAuthSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(goodToken));
    }

    @Test
    @Order(6)
    public void deleteAuthFailure() {
        Assertions.assertThrows(Exception.class, () -> authDAO.deleteAuth(""));
    }

    @Test
    @Order(7)
    public void deleteAllSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAll());
    }
}
