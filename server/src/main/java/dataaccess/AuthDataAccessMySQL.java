package dataaccess;

import model.AuthData;

public class AuthDataAccessMySQL implements AuthDataAccess {
    @Override
    public AuthData getAuth(String token) {
        return null;
    }

    @Override
    public void createAuth(AuthData data) {

    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void deleteAll() {

    }
}
