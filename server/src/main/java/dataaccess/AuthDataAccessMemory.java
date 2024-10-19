package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthDataAccessMemory implements AuthDataAccess {
    private ArrayList<AuthData> authList = new ArrayList<>();

    @Override
    public AuthData getAuth(String token) {
        for (AuthData authData : authList) {
            if (authData.authToken().equals(token)) {
                return authData;
            }
        }
        return null;
    }

    @Override
    public void createAuth(AuthData data) {
        authList.add(data);
    }

    @Override
    public boolean checkIfLoggedIn(String username) {
        for (AuthData auth: authList) {
            if (username.equals(auth.username())) {
                return true;
            }
        }
        return false;
    }
}
