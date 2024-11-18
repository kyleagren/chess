package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {
    private ChessClient client;
    private String serverUrl;
    private String loginStatus;

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
                    String token = client.getToken();
                    client = new PostLoginClient(serverUrl);
                    loginStatus = "LOGGED_IN";
                    client.setToken(token);
                    needsHelp = true;
                }
                if (result.contains("logged out")) {
                    client = new PreLoginClient(serverUrl);
                    loginStatus = "LOGGED_OUT";
                    needsHelp = true;
                }
                if (result.contains("successfully joined")) {
                    String token = client.getToken();
                    client = new InGameClient(serverUrl);
                    client.setToken(token);
                    client.eval("redraw");
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
}
