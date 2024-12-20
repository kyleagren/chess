package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exception.ResponseException;
import model.GameData;
import ui.EscapeSequences;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class InGameClient extends ChessClient {
    private ServerFacade server;
    private ServerMessageObserver observer;
    private WebSocketCommunicator ws;
    private String username;
    private String playerColor;
    private int gameNumber;

    public InGameClient(String serverUrl, ServerMessageObserver observer, String username, int gameNumber,
                        GameData currentGame, String token) throws ResponseException {
        server = new ServerFacade(serverUrl);
        this.observer = observer;
        this.ws = new WebSocketCommunicator(serverUrl, observer);
        this.username = username;
        this.gameNumber = gameNumber;
        if (Objects.equals(username, currentGame.whiteUsername())) {
            playerColor = "white";
            ws.joinGame(token, gameNumber);
        }
        else if (Objects.equals(username, currentGame.blackUsername())) {
            playerColor = "black";
            ws.joinGame(token, gameNumber);
        }
        else {
            ws.joinGame(token, gameNumber);
        }
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawBoard(params);
                case "leave" -> leaveGame(params);
                case "move" -> makeMove(params);
                case "resign" -> resign(params);
                case "highlight" -> highlightPossibleMoves(params);
                default -> help();
            };
        } catch (Exception e) {
            return "Something went wrong. Error: " + e.getMessage();
        }
    }

    @Override
    public String help() {
        return EscapeSequences.SET_TEXT_COLOR_GREEN + """
                - help
                - redraw
                - move (start and end position, followed by promotion piece. example: E4 E6 Queen)
                - resign (forfeits the game, ending it)
                - leave (leaves the game, but allows position to be filled by another player)
                - highlight (highlights valid moves for given position) <LETTER><NUMBER>
                """;
    }

    public String redrawBoard(String... params) {
        // Observers will be drawn from white point of view.
        BoardDrawer drawer = new BoardDrawer(getGame());
        if (playerColor.equals("black")) {
            System.out.print("\n" + SET_TEXT_COLOR_BLACK);
            drawer.drawBlackBoard(null);
            System.out.println();
        }
        else {
            System.out.print("\n" + SET_TEXT_COLOR_BLACK);
            drawer.drawWhiteBoard(null);
            System.out.println();
        }
        return "";
    }



    private String leaveGame(String... params) throws ResponseException {
        ws.leaveGame(getToken(), gameNumber);
        ws = null;
        return "Successfully left the game.";
    }

    private String makeMove(String... params) {
        ChessMove newMove;
        ChessPiece.PieceType promotionPiece = null;

        if (params.length < 2) {
            return "Invalid move syntax. Type the start position, a space, then the end position";
        }
        if (params.length > 2) {
            String promotion = params[1].toUpperCase();
            switch (promotion.charAt(0)) {
                case 'Q' -> promotionPiece = ChessPiece.PieceType.QUEEN;
                case 'K' -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                case 'B' -> promotionPiece = ChessPiece.PieceType.BISHOP;
                case 'R' -> promotionPiece = ChessPiece.PieceType.ROOK;
                default -> {
                    return "Invalid promotion piece <startPos> <endPos> <promotionPiece>";
                }
            }
        }
        String first = params[0];
        first = first.toUpperCase();
        String second = params[1];
        second = second.toUpperCase();
        int row1;
        int col1;
        int row2;
        int col2;
        if (first.length() != 2 || second.length() != 2) {
            return "Moves should only consist of 1 letter and 1 number";
        }
        row1 = Character.getNumericValue(first.charAt(1));
        if (row1 < 1 || row1 > 8) {
            return "Invalid number";
        }
        switch (first.charAt(0)) {
            case 'A' -> col1 = 1;
            case 'B' -> col1 = 2;
            case 'C' -> col1 = 3;
            case 'D' -> col1 = 4;
            case 'E' -> col1 = 5;
            case 'F' -> col1 = 6;
            case 'G' -> col1 = 7;
            case 'H' -> col1 = 8;
            default -> {
                return "Invalid letter. It must be A-H";
            }
        }
        row2 = Character.getNumericValue(second.charAt(1));
        if (row2 < 1 || row2 > 8) {
            return "Invalid number";
        }
        switch (second.charAt(0)) {
            case 'A' -> col2 = 1;
            case 'B' -> col2 = 2;
            case 'C' -> col2 = 3;
            case 'D' -> col2 = 4;
            case 'E' -> col2 = 5;
            case 'F' -> col2 = 6;
            case 'G' -> col2 = 7;
            case 'H' -> col2 = 8;
            default -> {
                return "Invalid letter. It must be A-H";
            }
        }
        ChessPosition startPos = new ChessPosition(row1, col1);
        ChessPosition endPos = new ChessPosition(row2, col2);
        newMove = new ChessMove(startPos, endPos, promotionPiece);
        try {
            ws.makeMove(getToken(), gameNumber, newMove);
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
        return "";
    }

    private String resign(String... params) throws ResponseException {
        Scanner scanner = new Scanner(System.in); // Create a Scanner for user input
        System.out.println("Are you sure you want to resign? Type 'yes' to confirm:");

        String input = scanner.nextLine().trim();
        input = input.toLowerCase();
        if (input.equals("yes")) {
            ws.resign(getToken(), gameNumber);
        }
        else {
            return "Resignation cancelled.";
        }
        return "";
    }

    private String highlightPossibleMoves(String... params) {
        if (params.length != 1) {
            return "Invalid input. Please input a start tile";
        }
        BoardDrawer drawer = new BoardDrawer(getGame());
        int col1;
        int row1;
        String upperWord = params[0].toUpperCase();
        switch (upperWord.charAt(0)) {
            case 'A' -> col1 = 1;
            case 'B' -> col1 = 2;
            case 'C' -> col1 = 3;
            case 'D' -> col1 = 4;
            case 'E' -> col1 = 5;
            case 'F' -> col1 = 6;
            case 'G' -> col1 = 7;
            case 'H' -> col1 = 8;
            default -> {
                return "Invalid letter. It must be A-H";
            }
        }
        row1 = Character.getNumericValue(upperWord.charAt(1));
        if (row1 < 1 || row1 > 8) {
            return "Invalid number";
        }
        ChessPosition startPos = new ChessPosition(row1, col1);

        Collection<ChessMove> validMoves = getGame().game().validMoves(startPos);

        return drawer.drawHighlighted(validMoves, playerColor);
    }
}
