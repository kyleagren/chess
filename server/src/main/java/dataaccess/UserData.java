package dataaccess;

public interface UserData {
    public model.UserData getUser(String username);

    public void createUser(model.UserData registerRequest);
}
