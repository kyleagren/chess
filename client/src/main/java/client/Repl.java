package client;

import chess.ChessGame;
import model.GameData;
import ui.EscapeSequences;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements ServerMessageObserver {
    private ChessClient client;
    private String serverUrl;
    private String loginStatus;
    private String username;

    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void run() {
        boolean needsHelp;
        client = new PreLoginClient(serverUrl);
        loginStatus = "LOGGED_OUT";

        System.out.println("Welcome to Chess. Sign in to start.");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                needsHelp = false;
                result = client.eval(line);

                if (result.contains("logged in") || result.contains("registered")) {
                    var tokens = result.toLowerCase().split(" ");
                    username = tokens[tokens.length - 1];
                    if (username != null && username.endsWith(".")) {
                        username = username.substring(0, username.length() - 1);
                    }
                    String token = client.getToken();
                    client = new PostLoginClient(serverUrl);
                    loginStatus = "LOGGED_IN";
                    client.setToken(token);
                    needsHelp = true;
                }
                if (result.contains("logged out")) {
                    username = null;
                    client = new PreLoginClient(serverUrl);
                    loginStatus = "LOGGED_OUT";
                    needsHelp = true;
                }
                if (result.contains("successfully joined")) {
                    var tokens = result.toLowerCase().split(" ");
                    int gameNumber = Integer.parseInt(tokens[1]);
                    String token = client.getToken();
                    GameData game = client.getGame();
                    client = new InGameClient(serverUrl, this, username, gameNumber, game, client.getToken());
                    client.setToken(token);
                    client.setGame(game);
                    needsHelp = true;
                }
                if (result.contains("observer")) {
                    var tokens = result.toLowerCase().split(" ");
                    int gameNumber = Integer.parseInt(tokens[1]);
                    String token = client.getToken();
                    GameData game = client.getGame();
                    client = new InGameClient(serverUrl, this, null,
                            gameNumber, game, client.getToken());
                    client.setToken(token);
                    client.setGame(game);
                    needsHelp = true;
                }
                if (result.contains("left the game")) {
                    String token = client.getToken();
                    client = new PostLoginClient(serverUrl);
                    client.setToken(token);
                    needsHelp = true;
                }
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + result);

                if (needsHelp) {
                    System.out.print('\n' + EscapeSequences.SET_TEXT_COLOR_GREEN);
                    System.out.print(client.eval("help"));
                }
            } catch (Throwable e) {
                String message = e.toString();
                System.out.print(message);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print((loginStatus.equals("LOGGED_OUT") ?
                EscapeSequences.SET_TEXT_COLOR_YELLOW :
                EscapeSequences.SET_TEXT_COLOR_BLUE) +
                "\n[" + loginStatus + "]" +
                EscapeSequences.RESET_BG_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((Notification) message).getMessageText());
            case ERROR -> displayError((message).getMessageText());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    private void displayNotification(String message) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + message);
        printPrompt();
    }

    private void displayError(String message) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message);
        printPrompt();
    }

    private void loadGame(GameData game) {
        client.setGame(game);
        client.eval("redraw");
        printPrompt();
    }
}
