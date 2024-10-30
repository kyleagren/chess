package service;

import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.UserDataAccessMemory;
import dataaccess.UserDataAccessMySQL;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.EmptySuccessResponse;

import java.util.UUID;

public class UserService {
    private final UserDataAccess userDataAccess = new UserDataAccessMySQL();
    private final AuthService authService = new AuthService();

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public AuthData register(UserData userData) throws Exception {
        var username = userData.username();
        UserData user = userDataAccess.getUser(username);
        AuthData data;

        if (user == null) {
            try {
                String hashedPassword = hashPassword(userData.password());
                UserData secureUser = new UserData(userData.username(), hashedPassword, userData.email());
                userDataAccess.createUser(secureUser);
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

        if (BCrypt.checkpw(password, user.password())) {
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
            UUID.fromString(token);
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
        try {
            userDataAccess.deleteAll();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
