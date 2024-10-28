package dataaccess;

import model.UserData;

public class UserDataAccessMySQL implements UserDataAccess {
    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(UserData registerRequest) throws DataAccessException {

    }

    @Override
    public void deleteAll() {

    }
}
