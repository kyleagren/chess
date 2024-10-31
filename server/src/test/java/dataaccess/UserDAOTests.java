package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

public class UserDAOTests extends DAOTest {
    UserDataAccess userDAO = new UserDataAccessMySQL();

    String username = "";
    String password = "";
    String email = "";

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
