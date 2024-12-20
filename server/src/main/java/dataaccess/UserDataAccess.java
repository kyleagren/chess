package dataaccess;

public interface UserDataAccess {
    model.UserData getUser(String username) throws DataAccessException;

    void createUser(model.UserData registerRequest) throws DataAccessException;

    void deleteAll() throws DataAccessException;
}
