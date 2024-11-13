package client;

import exception.ResponseException;

import java.util.Arrays;

public class PostLoginClient extends ChessClient {
    ServerFacade server;
    String token = null;

    public PostLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "play" -> joinGame(params);
                case "observe" -> observeGame(params);
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
                - logout
                - create <gameName>
                - list
                - play <gameNumber> <color>
                - observe <gameNumber> 
                - quit
                """;
    }

    public String logout(String... params) throws ResponseException {
        server.logout(getToken());
        return "";
    }

    public String createGame(String... params) {
        return "";
    }

    public String listGames(String... params) {
        return "";
    }

    public String joinGame(String... params) {
        return "";
    }

    public String observeGame(String... params) {
        return "";
    }
}
