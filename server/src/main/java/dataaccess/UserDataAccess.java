package dataaccess;

public interface UserDataAccess {
    model.UserData getUser(String username);

    void createUser(model.UserData registerRequest) throws DataAccessException;
}
