package server;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import spark.*;
import service.UserService;

public class Handler {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final AuthService authService = new AuthService();

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

    public Object login(Request req, Response res) {
        var userData = getBody(req, UserData.class);
        res.type("application/json");
        if (userData.username() == null || userData.password() == null) {
            res.status(400);
            ErrorResponse error = new ErrorResponse("Error: bad request");
            return new Gson().toJson(error);
        }
        try {
            Object result = userService.login(userData.username(), userData.password());
            return new Gson().toJson(result);
        } catch(Exception e) {
            if (e.getMessage().equals("incorrect password") || e.getMessage().equals("User does not exist")) {
                res.status(401);
                ErrorResponse error = new ErrorResponse("Error: unauthorized");
                return new Gson().toJson(error);
            }
            else {
                res.status(500);
                ErrorResponse error = new ErrorResponse("Error: " + e.getMessage());
                return new Gson().toJson(error);
            }
        }
    }

    public Object logout(Request req, Response res) {
        String token = req.headers("Authorization");
        res.type("application/json");
        try {
            Object result = userService.logout(token);
            return new Gson().toJson(result);
        } catch(Exception e) {
        if (e.getMessage().equals("not found") ||e.getMessage().equals("unauthorized")) {
            res.status(401);
            ErrorResponse error = new ErrorResponse("Error: unauthorized");
            return new Gson().toJson(error);
        }
        else {
            res.status(500);
            ErrorResponse error = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(error);
        }
    }
    }

    public Object createGame(Request req, Response res) {
        String token = req.headers("Authorization");
        res.type("application/json");
        var gameData = getBody(req, GameData.class);
        if (gameData.gameName() == null) {
            res.status(400);
            ErrorResponse error = new ErrorResponse("Error: bad request");
            return new Gson().toJson(error);
        }
        try {
            Object result = gameService.createGame(token, gameData.gameName());
            return new Gson().toJson(result);
        } catch (Exception e) {
            return exceptionHandler(e, res);
        }
    }

    public Object joinGame(Request req, Response res) {
        String token = req.headers("Authorization");
        res.type("application/json");
        var gameData = getBody(req, JoinGameRequestBody.class);
        if (gameData.gameID() <= 0 || (gameData.playerColor() == null) ||
                !(gameData.playerColor().equals("WHITE") || gameData.playerColor().equals("BLACK"))) {
            res.status(400);
            ErrorResponse error = new ErrorResponse("Error: bad request");
            return new Gson().toJson(error);
        }
        try {
            Object result = gameService.joinGame(token, gameData.gameID(), gameData.playerColor());
            return new Gson().toJson(result);
        } catch (Exception e) {
            if (e.getMessage().equals("not found")) {
                res.status(401);
                ErrorResponse error = new ErrorResponse("Error: unauthorized");
                return new Gson().toJson(error);
            }
            else if (e.getMessage().equals("already taken")) {
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

    public Object listGames(Request req, Response res) {
        String token = req.headers("Authorization");
        res.type("application/json");
        try {
            Object result = gameService.getGames(token);
            return new Gson().toJson(result);
        } catch (Exception e) {
            return exceptionHandler(e, res);
        }
    }

    private Object exceptionHandler(Exception e, Response res) {
        if (e.getMessage().equals("not found")) {
            res.status(401);
            ErrorResponse error = new ErrorResponse("Error: unauthorized");
            return new Gson().toJson(error);
        }
        else {
            res.status(500);
            ErrorResponse error = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(error);
        }
    }

    public Object clear(Request req, Response res) {
        res.type("application/json");
        userService.deleteAll();
        authService.deleteAll();
        gameService.deleteAll();
        return new Gson().toJson(new EmptySuccessResponse());
    }

    private static <T> T getBody(Request request, Class<T> classType) {
        var body = new Gson().fromJson(request.body(), classType);
        if (body ==  null) {
            throw new RuntimeException("Missing required body");
        }
        return body;
    }
}
