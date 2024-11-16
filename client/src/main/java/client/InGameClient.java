package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import exception.ResponseException;
import ui.EscapeSequences;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class InGameClient extends ChessClient {
    private ServerFacade server;
    ChessBoard board = new ChessBoard();
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
        return """
                - redraw
                - quit
                """;
    }

    static public String redrawBoard(String... params) {
        String defaultColor = "WHITE"; // Observers will be drawn from white point of view.
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

        for (int i = 0; i < boardRepresentation.length; i++) {
            ChessPiece.PieceType type;
            ChessGame.TeamColor color;

            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(i + 1);

            for (int j = 0; j < boardRepresentation.length; j++) {
                if (j % 2 == 0) {
                    System.out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    System.out.print(SET_BG_COLOR_BLACK);
                }

                if (boardRepresentation[i][j] != null) {
                    type = boardRepresentation[i][j].getPieceType();
                    color = boardRepresentation[i][j].getTeamColor();
                }
                else {
                    System.out.print(EMPTY);
                    continue;
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
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(i + 1);
            System.out.println(SET_BG_COLOR_BLACK);
        }
    }

    private void drawBlackBoard() {

    }
}
