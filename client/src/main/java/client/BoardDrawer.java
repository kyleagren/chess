package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_PAWN;

public class BoardDrawer {
    GameData gameData;
    public BoardDrawer(GameData data) {
        gameData = data;
    }

    public void drawWhiteBoard(Collection<ChessMove> validMoves) {
        ChessPiece[][] boardRepresentation = gameData.game().getBoard().getBoard();

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

            drawBoard(boardRepresentation, i, ChessGame.TeamColor.WHITE, validMoves);

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

    public void drawBlackBoard(Collection<ChessMove> validMoves) {
        ChessPiece[][] boardRepresentation = gameData.game().getBoard().getBoard();

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

            drawBoard(boardRepresentation, i, ChessGame.TeamColor.BLACK, validMoves);

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

    public void drawBoard(ChessPiece[][] boardRepresentation, int i,
                          ChessGame.TeamColor colorToDraw, Collection<ChessMove> validMoves) {
        ChessPiece.PieceType type;
        ChessGame.TeamColor color;
        ChessPosition start = null;
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startCol = -1;
        int startRow = -1;
        ArrayList<Integer> rowPositions = new ArrayList<>();
        ArrayList<Integer> colPositions = new ArrayList<>();
        if (validMoves != null) {
            moves = (ArrayList<ChessMove>) validMoves;
            start = moves.getFirst().getStartPosition();

            startCol = start.getColumn() - 1;
            startRow = start.getRow() - 1;
        }


        for (int j = 0; j < boardRepresentation.length; j++) {
            if (colorToDraw == ChessGame.TeamColor.WHITE) {
                if (i == startRow && j == startCol) {
                    System.out.print(SET_BG_COLOR_MAGENTA);
                }
                else if ((i + j) % 2 != 0) {
                    if (start != null && moves.contains(
                            new ChessMove(start, new ChessPosition(i + 1, j + 1), null))) {
                        System.out.print(SET_BG_COLOR_GREEN);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                }
                else {
                    if (start != null && moves.contains(
                            new ChessMove(start, new ChessPosition(i + 1, j + 1), null))) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }
            }
            else {
                if (i == startRow && (boardRepresentation.length - j) - 1 == startCol) {
                    System.out.print(SET_BG_COLOR_MAGENTA);
                }
                else if ((i + j) % 2 == 0) {
                    if (start != null && moves.contains(
                            new ChessMove(start, new ChessPosition(i + 1,
                                    boardRepresentation.length - j), null))) {
                        System.out.print(SET_BG_COLOR_GREEN);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                }
                else {
                    if (start != null && moves.contains(
                            new ChessMove(start, new ChessPosition(i + 1,
                                    boardRepresentation.length - j), null))) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }
            }
            if (colorToDraw == ChessGame.TeamColor.WHITE) {
                if (boardRepresentation[i][j] == null) {
                    System.out.print(EMPTY);
                    continue;
                }
                type = boardRepresentation[i][j].getPieceType();
                color = boardRepresentation[i][j].getTeamColor();
            }
            else {
                if (boardRepresentation[i][(boardRepresentation.length - 1) - j] == null) {
                    System.out.print(EMPTY);
                    continue;
                }
                type = boardRepresentation[i][(boardRepresentation.length - 1) - j].getPieceType();
                color = boardRepresentation[i][(boardRepresentation.length - 1) - j].getTeamColor();
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

    public String drawHighlighted(Collection<ChessMove> validMoves, String color) {
        System.out.print(SET_TEXT_COLOR_BLACK);
        if (validMoves.isEmpty()) {
            return "No valid moves to highlight.";
        }
        if (color.equals("black")) {
            drawBlackBoard(validMoves);
        }
        else {
            drawWhiteBoard(validMoves);
        }
        return "";
    }
}
