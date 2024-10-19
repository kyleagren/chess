package service;

import dataaccess.UserDataMemory;
import model.UserData;

public class UserService {
    private final UserDataMemory userDataAccessMemory = new UserDataMemory();

    public void register(UserData registerRequest) {
        var username = registerRequest.username();
        var password = registerRequest.password();
        var email = registerRequest.email();

        model.UserData user = userDataAccessMemory.getUser(username);
        if (user == null) {
            userDataAccessMemory.createUser(registerRequest);
        }
    }
}
