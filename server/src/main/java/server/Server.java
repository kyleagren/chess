package server;

import com.google.gson.Gson;
import model.UserData;
import spark.*;

public class Server {

    private final Handler handler = new Handler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here
        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public Object register(Request req, Response res) {
        this.handler.register(req, res); // TODO is this the problem child? No handler = new Handler() anywhere
        return null;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
