package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import spark.*;
import service.UserService;

public class Handler {
    private final UserService userService = new UserService();

    public AuthData register(Request req, Response res) {
        var userData = getBody(req, UserData.class);
        return userService.register(userData);
    }

    private static <T> T getBody(Request request, Class<T> classType) {
        var body = new Gson().fromJson(request.body(), classType);
        if (body ==  null) {
            throw new RuntimeException("Missing required body");
        }
        return body;
    }
}
