package service;

import dataaccess.AuthDataAccessMemory;
import dataaccess.AuthDataAccessMySQL;
import dataaccess.DataAccessException;
import model.AuthData;
import dataaccess.AuthDataAccess;

import java.util.UUID;

public class AuthService {
    private final AuthDataAccess authDataAccess = new AuthDataAccessMySQL();

    public AuthData createAuth(String username) throws Exception {
        if (username == null) {
            throw new Exception("invalid username");
        }
        String token = UUID.randomUUID().toString();
        model.AuthData data = new model.AuthData(token, username);
        authDataAccess.createAuth(data);
        return data;
    }

    public AuthData getAuth(String token) throws DataAccessException {
        return authDataAccess.getAuth(token);
    }

    public void deleteAuth(String token) throws DataAccessException {
            try {
                authDataAccess.deleteAuth(token);
            } catch (DataAccessException e) {
                throw new DataAccessException(e.getMessage());
            }
    }

    public void deleteAll() {
        try {
            authDataAccess.deleteAll();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
