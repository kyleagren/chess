package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import ui.EscapeSequences;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class InGameClient extends ChessClient {
    private ServerFacade server;
    private String playerColor;
    ChessBoard board = new ChessBoard(true);

    public InGameClient(String serverUrl, String color, int gameNumber) {
        server = new ServerFacade(serverUrl);
        playerColor = color;
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
            // TODO - for Phase 6 this probably needs to be a ResponseException.
            return "Something went wrong. Error: " + e.getMessage();
        }
    }

    @Override
    public String help() {
        return EscapeSequences.SET_TEXT_COLOR_GREEN + """
                - help
                - redraw
                - move (start and end position for the preferred move) <LETTER><NUMBER> -> <LETTER><NUMBER>
                - resign (forfeits the game, ending it)
                - leave (leaves the game, but allows position to be filled by another player)
                - highlight (highlights valid moves for given position) <LETTER><NUMBER>
                """;
    }

    public String redrawBoard(String... params) {
        String defaultColor = "WHITE"; // Observers will be drawn from white point of view.
        if (playerColor.equals("black")) {
            System.out.print(SET_TEXT_COLOR_BLACK);
            drawBlackBoard();
            System.out.println();
        }
        else {
            System.out.print(SET_TEXT_COLOR_BLACK);
            drawWhiteBoard();
            System.out.println();
        }
        return "";
    }

    private void drawWhiteBoard() {
        ChessPiece[][] boardRepresentation = board.getBoard();

        System.out.print(SET_BG_COLOR_YELLOW);
        System.out.print(EMPTY);
        System.out.print(" a ");
        System.out.print(" b  ");
        System.out.print(" c  ");
        System.out.print(" d ");
        System.out.print(" e  ");
        System.out.print(" f ");
        System.out.print(" g  ");
        System.out.print(" h ");
        System.out.print(EMPTY);
        System.out.println(SET_BG_COLOR_BLACK);

        for (int i = boardRepresentation.length - 1; i >= 0; i--) {
            System.out.print(SET_BG_COLOR_YELLOW);
            System.out.print(" " + (i + 1) + " ");

            drawBoard(boardRepresentation, i, ChessGame.TeamColor.WHITE);

            System.out.print(SET_BG_COLOR_YELLOW);
            System.out.print(" " + (i + 1) + " ");
            System.out.println(SET_BG_COLOR_BLACK);
        }

        System.out.print(SET_BG_COLOR_YELLOW);
        System.out.print(EMPTY);
        System.out.print(" a ");
        System.out.print(" b  ");
        System.out.print(" c  ");
        System.out.print(" d ");
        System.out.print(" e  ");
        System.out.print(" f ");
        System.out.print(" g  ");
        System.out.print(" h ");
        System.out.print(EMPTY);
        System.out.println(SET_BG_COLOR_BLACK);
    }

    private void drawBlackBoard() {
        ChessPiece[][] boardRepresentation = board.getBoard();

        System.out.print(SET_BG_COLOR_YELLOW);
        System.out.print(EMPTY);
        System.out.print(" h ");
        System.out.print(" g  ");
        System.out.print(" f  ");
        System.out.print(" e ");
        System.out.print(" d  ");
        System.out.print(" c ");
        System.out.print(" b  ");
        System.out.print(" a ");
        System.out.print(EMPTY);
        System.out.println(SET_BG_COLOR_BLACK);

        for (int i = 0; i < boardRepresentation.length; i++) {
            System.out.print(SET_BG_COLOR_YELLOW);
            System.out.print(" " + (i + 1) + " ");

            drawBoard(boardRepresentation, i, ChessGame.TeamColor.BLACK);

            System.out.print(SET_BG_COLOR_YELLOW);
            System.out.print(" " + (i + 1) + " ");
            System.out.println(SET_BG_COLOR_BLACK);
        }

        System.out.print(SET_BG_COLOR_YELLOW);
        System.out.print(EMPTY);
        System.out.print(" h ");
        System.out.print(" g  ");
        System.out.print(" f  ");
        System.out.print(" e ");
        System.out.print(" d  ");
        System.out.print(" c ");
        System.out.print(" b  ");
        System.out.print(" a ");
        System.out.print(EMPTY);
        System.out.println(SET_BG_COLOR_BLACK);
    }

    private void drawBoard(ChessPiece[][] boardRepresentation, int i, ChessGame.TeamColor colorToDraw) {
        ChessPiece.PieceType type;
        ChessGame.TeamColor color;

        for (int j = 0; j < boardRepresentation.length; j++) {
            if (colorToDraw == ChessGame.TeamColor.WHITE) {
                if ((i + j) % 2 != 0) {
                    System.out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
            }
            else {
                if ((i + j) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
            }

            if (boardRepresentation[i][j] == null) {
                System.out.print(EMPTY);
                continue;
            }
            else {
                if (colorToDraw == ChessGame.TeamColor.WHITE) {
                    type = boardRepresentation[i][j].getPieceType();
                    color = boardRepresentation[i][j].getTeamColor();
                }
                else {
                    type = boardRepresentation[i][(boardRepresentation.length - 1) - j].getPieceType();
                    color = boardRepresentation[i][(boardRepresentation.length - 1) - j].getTeamColor();
                }
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

    private String leaveGame(String... params) {
        return "leave";
    }

    private String makeMove(String... params) {

    }

    private String resign(String... params) {

    }

    private String highlightPossibleMoves(String... params) {

    }
}
