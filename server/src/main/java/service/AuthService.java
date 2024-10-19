package service;

import dataaccess.AuthDataAccessMemory;
import dataaccess.DataAccessException;
import model.UserData;
import dataaccess.AuthDataAccess;

import java.util.UUID;

public class AuthService {
    private final AuthDataAccess authDataAccess = new AuthDataAccessMemory();

    public model.AuthData createAuth(UserData userData) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        String username = userData.username();
        model.AuthData data = new model.AuthData(token, username);
        authDataAccess.createAuth(data);
        return data;
    }
}
