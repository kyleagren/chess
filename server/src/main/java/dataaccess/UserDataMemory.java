package dataaccess;

import java.util.ArrayList;

public class UserDataMemory implements UserData {
    private ArrayList<model.UserData> userList = new ArrayList<>();

    public model.UserData getUser(String username) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).username().equals(username)) {
                return userList.get(i);
            }
        }
        return null;
    }

    public void createUser(model.UserData userData) {

    }
}
