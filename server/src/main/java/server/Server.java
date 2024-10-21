package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import spark.*;

public class Server {

    private final Handler handler = new Handler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public Object register(Request req, Response res) {
        return this.handler.register(req, res);
    }

    public Object login(Request req, Response res) {
        return this.handler.login(req, res);
    }

    public Object logout(Request req, Response res) {
        return this.handler.logout(req, res);
    }

    public Object createGame(Request req, Response res) {
        return this.handler.createGame(req, res);
    }

    public Object joinGame(Request req, Response res) {
        return this.handler.joinGame(req, res);
    }

    public Object listGames(Request req, Response res) {
        return this.handler.listGames(req, res);
    }

    public Object clear(Request req, Response res) {
        return this.handler.clear(req, res);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
