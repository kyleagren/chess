package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        if (type == PieceType.BISHOP) {
            // Move up and to the right
            while (currentRow < 8 && currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol + 1);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
                else if (pieceColor != board.getPiece(newPosition).pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                    break;
                }
                else {
                    break;
                }
                currentRow++;
                currentCol++;
                }

            }
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
            // Move down and to the right
            while (currentRow > 1 && currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol + 1);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
                else if (pieceColor != board.getPiece(newPosition).pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                    break;
                }
                else {
                    break;
                }
                currentRow--;
                currentCol++;
            }
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
            // Move down and to the left
            while (currentRow > 1 && currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol - 1);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
                else if (pieceColor != board.getPiece(newPosition).pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                    break;
                }
                else {
                    break;
                }
                currentRow--;
                currentCol--;
            }
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
            // Move up and to the left
            while (currentRow < 8 && currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol - 1);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
                else if (pieceColor != board.getPiece(newPosition).pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                    break;
                }
                else {
                    break;
                }
                currentRow++;
                currentCol--;
            }
        return possibleMoves;
    }
}
