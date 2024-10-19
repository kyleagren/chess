package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    AuthData getAuth(String token);
    void createAuth(AuthData data);

    boolean checkIfLoggedIn(String username);

    void deleteAuth(String token) throws DataAccessException;
}
