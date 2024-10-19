package service;

import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.UserDataAccessMemory;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDataAccess userDataAccess = new UserDataAccessMemory();
    private final AuthService authService = new AuthService();

    public AuthData register(UserData userData) {
        var username = userData.username();

        model.UserData user = userDataAccess.getUser(username);
        AuthData data;
        if (user == null) {
            try {
                userDataAccess.createUser(userData);
            } catch (DataAccessException e) {
                System.out.print(e.getMessage());
                return null;
            }
            try {
                data = authService.createAuth(userData);
            } catch (DataAccessException e) {
                System.out.print(e.getMessage());
                return null;
            }
        }
        else {
            return null;
        }

        return data;
    }
}
