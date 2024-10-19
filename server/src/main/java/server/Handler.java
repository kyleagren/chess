package server;

import com.google.gson.Gson;
import model.UserData;
import spark.*;
import service.UserService;

public class Handler {
    private final UserService userService = new UserService();
    public void register(Request req, Response Res) {
        var bodyObj = getBody(req, UserData.class);
        userService.register(bodyObj);
    }

    private static <T> T getBody(Request request, Class<T> classType) {
        var body = new Gson().fromJson(request.body(), classType);
        if (body ==  null) {
            throw new RuntimeException("Missing required body");
        }
        return body;
    }
}
