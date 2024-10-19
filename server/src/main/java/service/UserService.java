package service;

import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.UserDataAccessMemory;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDataAccess userDataAccess = new UserDataAccessMemory();
    private final AuthService authService = new AuthService();

    public AuthData register(UserData userData) throws Exception{
        var username = userData.username();
        UserData user = userDataAccess.getUser(username);
        AuthData data;

        if (user == null) {
            try {
                userDataAccess.createUser(userData);
            } catch (DataAccessException e) {
                throw new Exception(e.getMessage());
            }
            try {
                data = authService.createAuth(userData);
            } catch (DataAccessException e) {
                throw new Exception(e.getMessage());
            }
        }
        else {
            throw new Exception("already taken");
        }
        return data;
    }
}
