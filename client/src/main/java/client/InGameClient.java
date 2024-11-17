package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import ui.EscapeSequences;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class InGameClient extends ChessClient {
    private ServerFacade server;
    ChessBoard board = new ChessBoard(); // refactor to get the board from the db
//    int[][] boardRepresentation = new int[8][8];

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
        return EscapeSequences.SET_TEXT_COLOR_GREEN + """
                - redraw
                - quit
                """;
    }

    public String redrawBoard(String... params) {
        String defaultColor = "WHITE"; // Observers will be drawn from white point of view.
        drawWhiteBoard();
        drawBlackBoard();
        return "";
    }

    private void drawWhiteBoard() {
        ChessPiece[][] boardRepresentation = board.getBoard();

        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(" ");
        System.out.print("h");
        System.out.print("g");
        System.out.print("f");
        System.out.print("e");
        System.out.print("d");
        System.out.print("c");
        System.out.print("b");
        System.out.print("a");
        System.out.print(" ");
        System.out.println(SET_BG_COLOR_BLACK);

        for (int i = 0; i < boardRepresentation.length; i++) {
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(i + 1);

            drawBoard(boardRepresentation);

            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(i + 1);
            System.out.println(SET_BG_COLOR_BLACK);
        }

        System.out.print(" ");
        System.out.print("h");
        System.out.print("g");
        System.out.print("f");
        System.out.print("e");
        System.out.print("d");
        System.out.print("c");
        System.out.print("b");
        System.out.print("a");
        System.out.print(" ");
    }

    private void drawBlackBoard() {
        ChessPiece[][] boardRepresentation = board.getBoard();

        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(" ");
        System.out.print("a");
        System.out.print("b");
        System.out.print("c");
        System.out.print("d");
        System.out.print("e");
        System.out.print("f");
        System.out.print("g");
        System.out.print("h");
        System.out.print(" ");
        System.out.println(SET_BG_COLOR_BLACK);

        for (int i = 0; i < boardRepresentation.length; i++) {
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(boardRepresentation.length - i);

            drawBoard(boardRepresentation);

            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(boardRepresentation.length - i);
            System.out.println(SET_BG_COLOR_BLACK);
        }

        System.out.print(" ");
        System.out.print("a");
        System.out.print("b");
        System.out.print("c");
        System.out.print("d");
        System.out.print("e");
        System.out.print("f");
        System.out.print("g");
        System.out.print("h");
        System.out.print(" ");
    }

    private void drawBoard(ChessPiece[][] boardRepresentation) {
        ChessPiece.PieceType type;
        ChessGame.TeamColor color;

        for (int i = 0; i < boardRepresentation.length; i++) {
            if (i % 2 == 0) {
                System.out.print(SET_BG_COLOR_WHITE);
            }
            else {
                System.out.print(SET_BG_COLOR_BLACK);
            }

            if (boardRepresentation[i][i] == null) {
                System.out.print(EMPTY);
                continue;
            }
            else {
                type = boardRepresentation[i][i].getPieceType();
                color = boardRepresentation[i][i].getTeamColor();
            }
            if (color == ChessGame.TeamColor.WHITE) {
                switch (type) {
                    case ChessPiece.PieceType.QUEEN -> System.out.print(WHITE_QUEEN);
                    case ChessPiece.PieceType.KING  -> System.out.print(WHITE_KING);
                    case ChessPiece.PieceType.BISHOP  -> System.out.print(WHITE_BISHOP);
                    case ChessPiece.PieceType.KNIGHT  -> System.out.print(WHITE_KNIGHT);
                    case ChessPiece.PieceType.ROOK  -> System.out.print(WHITE_ROOK);
                    case ChessPiece.PieceType.PAWN  -> System.out.print(WHITE_PAWN);
                }
            }
            else if (color == ChessGame.TeamColor.BLACK) {
                switch (type) {
                    case ChessPiece.PieceType.QUEEN -> System.out.print(BLACK_QUEEN);
                    case ChessPiece.PieceType.KING -> System.out.print(BLACK_KING);
                    case ChessPiece.PieceType.BISHOP -> System.out.print(BLACK_BISHOP);
                    case ChessPiece.PieceType.KNIGHT -> System.out.print(BLACK_KNIGHT);
                    case ChessPiece.PieceType.ROOK -> System.out.print(BLACK_ROOK);
                    case ChessPiece.PieceType.PAWN -> System.out.print(BLACK_PAWN);
                }
            }
        }
    }
}
