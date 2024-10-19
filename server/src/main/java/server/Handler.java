package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import spark.*;
import service.UserService;

public class Handler {
    private final UserService userService = new UserService();

    public Object register(Request req, Response res) {
        var userData = getBody(req, UserData.class);
        res.type("application/json");
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            res.status(400);
            ErrorResponse error = new ErrorResponse("Error: bad request");
            return new Gson().toJson(error);
        }
        try {
            Object result = userService.register(userData);
            return new Gson().toJson(result);
        } catch(Exception e) {
            if (e.getMessage().equals("already taken")) {
                res.status(403);
                ErrorResponse error = new ErrorResponse("Error: already taken");
                return new Gson().toJson(error);
            }
            else {
                res.status(500);
                ErrorResponse error = new ErrorResponse("Error: " + e.getMessage());
                return new Gson().toJson(error);
            }
        }
    }

    private static <T> T getBody(Request request, Class<T> classType) {
        var body = new Gson().fromJson(request.body(), classType);
        if (body ==  null) {
            throw new RuntimeException("Missing required body");
        }
        return body;
    }
}
