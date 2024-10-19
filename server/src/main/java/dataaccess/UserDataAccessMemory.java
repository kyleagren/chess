package dataaccess;

import java.util.ArrayList;

public class UserDataAccessMemory implements UserDataAccess {
    private final ArrayList<model.UserData> userList = new ArrayList<>();

    @Override
    public model.UserData getUser(String username) {
        for (model.UserData userData : userList) {
            if (userData.username().equals(username)) {
                return userData;
            }
        }
        return null;
    }

    @Override
    public void createUser(model.UserData userData) throws DataAccessException {
        if (getUser(userData.username()) == null) {
            userList.add(userData);
        }
        else throw new DataAccessException("User already exists");
    }
}
