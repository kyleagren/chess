package client;

import chess.ChessGame;
import ui.EscapeSequences;
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
                    String currentColor = null;
                    ChessGame game = client.getGame();
                    if (result.contains("white")) {
                        currentColor = "white";
                    }
                    else if (result.contains("black")) {
                        currentColor = "black";
                    }
                    client = new InGameClient(serverUrl, this, username, currentColor, gameNumber);
                    client.setToken(token);
                    client.setGame(game);
                    client.eval("redraw");
                    needsHelp = true;
                }
                if (result.contains("observer")) {
                    var tokens = result.toLowerCase().split(" ");
                    int gameNumber = Integer.parseInt(tokens[1]);
                    String token = client.getToken();
                    ChessGame game = client.getGame();
                    client = new InGameClient(serverUrl, this, null,null, gameNumber);
                    client.setToken(token);
                    client.setGame(game);
                    client.eval("redraw");
                    needsHelp = true;
                }
                if (result.contains("leave")) {
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
    public void notifyWS(ServerMessage message) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + message);
        printPrompt();
    }
}
