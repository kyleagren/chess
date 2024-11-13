package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {
    private ChessClient client;
    private String serverUrl;

    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void run() {
        client = new PreLoginClient(serverUrl);
        System.out.println("Welcome to Chess. Sign in to start.");

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (result.contains("logged") || result.contains("registered")) {
                    client = new PostLoginClient(serverUrl);
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
        System.out.print("\n" + EscapeSequences.RESET_BG_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
