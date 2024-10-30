package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    AuthData getAuth(String token) throws DataAccessException;
    void createAuth(AuthData data);

    void deleteAuth(String token) throws DataAccessException;

    void deleteAll();
}
