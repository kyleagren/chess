package client;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.Arrays;

public class PreLoginClient extends ChessClient {
    private final ServerFacade server;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    @Override
    public String help() {
        return """
                - register <username> <password> <email>
                - login <username> <password>
                - quit
                """;
    }

    public String login(String... params) throws ResponseException {
        String username = "";
        String password;
        if (params.length >= 2) {
            username = params[0];
            password = params[1];

            UserData data = new UserData(username, password, null);

            try {
                AuthData auth = server.login(data);
                setToken(auth.authToken());
            } catch (ResponseException e) {
                return e.getMessage();
            }
            return String.format("You have logged in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> and <password>");
    }

    public String register(String... params) throws ResponseException {
        String username = "";
        String password;
        String email;
        if (params.length >= 3) {
            username = params[0];
            password = params[1];
            email = params[2];

            UserData data = new UserData(username, password, email);
            try {
                AuthData auth = server.login(data);
                setToken(auth.authToken());
            } catch (ResponseException e) {
                return e.getMessage();
            }
            return String.format("You have registered as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username>");
    }
}
