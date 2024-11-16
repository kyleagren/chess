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
        client = new PreLoginClient(serverUrl);
        loginStatus = "LOGGED_OUT";
        System.out.println("Welcome to Chess. Sign in to start.");
        System.out.println(client.help());
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (result.contains("logged in") || result.contains("registered")) {
                    String token = client.getToken();
                    client = new PostLoginClient(serverUrl);
                    loginStatus = "LOGGED_IN";
                    client.setToken(token);
                }
                if (result.contains("logged out")) {
                    client = new PreLoginClient(serverUrl);
                    loginStatus = "LOGGED_OUT";
                }
                if (result.contains("successfully joined")) {
                    client = new InGameClient(serverUrl);
                }
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                String message = e.toString();
                System.out.print(message);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n[" + loginStatus + "]" + EscapeSequences.RESET_BG_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
