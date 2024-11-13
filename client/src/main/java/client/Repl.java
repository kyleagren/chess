package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess. Sign in to start.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
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
