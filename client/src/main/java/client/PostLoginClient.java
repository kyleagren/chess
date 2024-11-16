package client;

import exception.ResponseException;
import model.GameData;
import response.GamesListResponse;
import response.JoinGameRequestBody;
import response.TruncatedGameData;

import java.util.ArrayList;
import java.util.Arrays;

public class PostLoginClient extends ChessClient {
    private ServerFacade server;
    private ArrayList<Integer> gameIdMap = new ArrayList<>();

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
            return "Something went wrong. Error: " + e.getMessage();
        }
    }

    @Override
    public String help() {
        return """
                - logout
                - create <gameName>
                - list
                - play <gameNumber> <WHITE|BLACK>
                - observe <gameNumber>
                - quit
                """;
    }

    public String logout(String... params) throws ResponseException {
        try {
            server.logout(getToken());
        } catch (ResponseException e) {
            return "Failed to logout. Error: " + e.getMessage();
        }
        return "You have logged out successfully";
    }

    public String createGame(String... params) throws ResponseException {
        String gameName;
        if (params.length >= 1) {
            gameName = params[0];

            // I actually don't like this at all, but I've kind of come too far to turn back now.
            GameData game = new GameData(0, null, null, gameName, null);

            try {
                server.createGame(game, getToken());
            } catch (ResponseException e) {
                return "Failed to create game. Error: " + e.getMessage();
            }
            return String.format("Game %s successfully created.", gameName);
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    public String listGames(String... params) throws ResponseException {
        GamesListResponse response = server.listGames(getToken());
        ArrayList<TruncatedGameData> games = response.listGames();
        StringBuilder builder = new StringBuilder();
        if (games.isEmpty()) {
            return "No games";
        }
        for (int i = 0; i < games.size(); i++) {
            TruncatedGameData currentGame = games.get(i);
            if (gameIdMap.get(i) != currentGame.gameID()) {
                gameIdMap.add(i, currentGame.gameID());
            }
            builder.append(String.format("%d. Game name: %s\n White player: %s\n Black player: %s", i + 1,
                    currentGame.gameName(), currentGame.whiteUsername() != null ? currentGame.whiteUsername() : "None",
                    currentGame.blackUsername() != null ? currentGame.whiteUsername() : "None"));
        }
        return builder.toString();
    }

    public String joinGame(String... params) throws ResponseException{
        if (params.length >= 2) {
            String gameNumber = params[0];
            String color = params[1];
            int convertedGameNumber = 0;

            if (color.equals("BLACK" ) || color.equals("WHITE")) {
                throw new ResponseException(400, "Invalid color");
            }

            try {
                convertedGameNumber = Integer.parseInt(gameNumber);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Invalid number");
            }

            JoinGameRequestBody request = new JoinGameRequestBody(color, convertedGameNumber);

            try {
                server.joinGame(request, getToken());
            } catch (ResponseException e) {
                return "Failed to join game. Error: " + e.getMessage();
            }
            return String.format("Game %s successfully joined as the %s player.", gameNumber, color);
        }
        throw new ResponseException(400, "Expected: <gameNumber> <color (WHITE | BLACK)>");
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            String gameNumber = params[0];
            int convertedGameNumber = 0;

            try {
                convertedGameNumber = Integer.parseInt(gameNumber);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Invalid number");
            }

            return String.format("Game %s successfully joined as an observer.", gameNumber);
        }
        throw new ResponseException(400, "Expected: <gameNumber>");
    }
}
