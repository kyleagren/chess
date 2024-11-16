package client;

import exception.ResponseException;

import java.util.Arrays;

public class InGameClient extends ChessClient {
    private ServerFacade server;

    public InGameClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                case "redraw" -> redrawBoard(params);
                default -> help();
            };
        } catch (Exception e) {
            // TODO - for Phase 6 this probably needs to be a ResponseException.
            return "Something went wrong. Error: " + e.getMessage();
        }
    }

    @Override
    public String help() {
        return """
                - redraw
                - quit
                """;
    }

    private String redrawBoard(String... params) {
        String defaultColor = "WHITE"; // Observers will be drawn from white point of view.
        return "";
    }
}
