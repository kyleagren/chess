package service;

import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.UserDataAccessMemory;
import model.AuthData;
import model.UserData;
import server.EmptySuccessResponse;

import java.util.UUID;

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
                data = authService.createAuth(userData.username());
            } catch (DataAccessException e) {
                throw new Exception(e.getMessage());
            }
        }
        else {
            throw new Exception("already taken");
        }
        return data;
    }

    public AuthData login(String username, String password) throws Exception {
        UserData user = userDataAccess.getUser(username);
        AuthData data;

        if (user == null) {
            throw new Exception("User does not exist");
        }
        if (password.equals(user.password())) {
//            if (authService.checkIfLoggedIn(username)) {
//                throw new Exception("already logged in");
//            }
            try {
                data = authService.createAuth(username);
            } catch (DataAccessException e) {
                throw new Exception(e.getMessage());
            }
        }
        else {
            throw new Exception("incorrect password");
        }
        return data;
    }

    public Object logout(String token) throws Exception {
        try{
            UUID uuid = UUID.fromString(token);
            if (authService.getAuth(token) != null) {
                authService.deleteAuth(token);
                return new EmptySuccessResponse();
            }
            throw new Exception("unauthorized");
        } catch (DataAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteAll() {
        userDataAccess.deleteAll();
    }
}
