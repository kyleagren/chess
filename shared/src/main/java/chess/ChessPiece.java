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

    public ChessPiece(ChessPiece piece) {
        this.pieceColor = piece.pieceColor;
        this.type = piece.type;
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
        if (type == PieceType.QUEEN || type == PieceType.BISHOP) {
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
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
        }
        if (type == PieceType.KING) {
            // Move up
            if (currentRow < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move up and right
            if (currentRow < 8 && currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol + 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move right
            if (currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol + 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move down and right
            if (currentRow > 1 && currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol + 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move down
            if (currentRow > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move down and left
            if (currentRow > 1 && currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol - 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move left
            if (currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol - 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move left and up
            if (currentRow < 8 && currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol - 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
        }
        if (type == PieceType.KNIGHT) {
            // Move up 2 and right 1
            if (currentRow < 7 && currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow + 2, currentCol + 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move up 1 and right 2
            if (currentRow < 8 && currentCol < 7) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol + 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move down 1 and right 2
            if (currentRow > 1 && currentCol < 7) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol + 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move down 2 and right 1
            if (currentRow > 2 && currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow - 2, currentCol + 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move down 2 and left 1
            if (currentRow > 2 && currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow - 2, currentCol - 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move down 1 and left 2
            if (currentRow > 1 && currentCol > 2) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol - 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move up 1 and left 2
            if (currentRow < 8 && currentCol > 2) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol - 2);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // Move up 2 and left 1
            if (currentRow < 7 && currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow + 2, currentCol - 1);
                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
        }
        if (type == PieceType.PAWN) {
            // White side moves
            // Move forward one or two if it hasn't moved yet
            if (currentRow == 2 && pieceColor == ChessGame.TeamColor.WHITE) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove1);
                    newPosition = new ChessPosition(currentRow + 2, currentCol);
                    if (board.getPiece(newPosition) == null) {
                        ChessMove possibleMove2 = new ChessMove(myPosition, newPosition, null);
                        possibleMoves.add(possibleMove2);
                    }
                }
            }
            // Move forward one if it has already moved
            if (currentRow > 2 && currentRow < 7 && pieceColor == ChessGame.TeamColor.WHITE) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // If we reach the opposite side, include promotion piece
            if (currentRow == 7 && pieceColor == ChessGame.TeamColor.WHITE) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition, PieceType.BISHOP);
                    possibleMoves.add(possibleMove1);
                    ChessMove possibleMove2 = new ChessMove(myPosition, newPosition, PieceType.KNIGHT);
                    possibleMoves.add(possibleMove2);
                    ChessMove possibleMove3 = new ChessMove(myPosition, newPosition, PieceType.ROOK);
                    possibleMoves.add(possibleMove3);
                    ChessMove possibleMove4 = new ChessMove(myPosition, newPosition, PieceType.QUEEN);
                    possibleMoves.add(possibleMove4);
                }
            }
            // Move diagonal one if a piece can be captured
            if (currentRow < 7 && currentCol > 1 && currentCol < 8 && pieceColor == ChessGame.TeamColor.WHITE) {
                ChessPosition newPosition1 = new ChessPosition(currentRow + 1, currentCol - 1);
                ChessPosition newPosition2 = new ChessPosition(currentRow + 1, currentCol + 1);
                if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition1, null);
                    possibleMoves.add(possibleMove);
                }
                if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition2, null);
                    possibleMoves.add(possibleMove);
                }
            }
            if (currentRow == 7 && currentCol > 1 && currentCol < 8 && pieceColor == ChessGame.TeamColor.WHITE) {
                ChessPosition newPosition1 = new ChessPosition(currentRow + 1, currentCol - 1);
                ChessPosition newPosition2 = new ChessPosition(currentRow + 1, currentCol + 1);
                if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != pieceColor) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition1, PieceType.BISHOP);
                    possibleMoves.add(possibleMove1);
                    ChessMove possibleMove2 = new ChessMove(myPosition, newPosition1, PieceType.KNIGHT);
                    possibleMoves.add(possibleMove2);
                    ChessMove possibleMove3 = new ChessMove(myPosition, newPosition1, PieceType.ROOK);
                    possibleMoves.add(possibleMove3);
                    ChessMove possibleMove4 = new ChessMove(myPosition, newPosition1, PieceType.QUEEN);
                    possibleMoves.add(possibleMove4);
                }
                if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != pieceColor) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition2, PieceType.BISHOP);
                    possibleMoves.add(possibleMove1);
                    ChessMove possibleMove2 = new ChessMove(myPosition, newPosition2, PieceType.KNIGHT);
                    possibleMoves.add(possibleMove2);
                    ChessMove possibleMove3 = new ChessMove(myPosition, newPosition2, PieceType.ROOK);
                    possibleMoves.add(possibleMove3);
                    ChessMove possibleMove4 = new ChessMove(myPosition, newPosition2, PieceType.QUEEN);
                    possibleMoves.add(possibleMove4);
                }
            }

            // Black side moves
            // Move forward(down) one or two if it hasn't moved yet
            if (currentRow == 7 && pieceColor == ChessGame.TeamColor.BLACK) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove1);
                    newPosition = new ChessPosition(currentRow - 2, currentCol);
                    if (board.getPiece(newPosition) == null) {
                        ChessMove possibleMove2 = new ChessMove(myPosition, newPosition, null);
                        possibleMoves.add(possibleMove2);
                    }
                }
            }
            // Move forward(down) one if it has already moved
            if (currentRow < 7 && currentRow > 2 && pieceColor == ChessGame.TeamColor.BLACK) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                    possibleMoves.add(possibleMove);
                }
            }
            // If we reach the opposite side, include promotion piece
            if (currentRow == 2 && pieceColor == ChessGame.TeamColor.BLACK) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition, PieceType.BISHOP);
                    possibleMoves.add(possibleMove1);
                    ChessMove possibleMove2 = new ChessMove(myPosition, newPosition, PieceType.KNIGHT);
                    possibleMoves.add(possibleMove2);
                    ChessMove possibleMove3 = new ChessMove(myPosition, newPosition, PieceType.ROOK);
                    possibleMoves.add(possibleMove3);
                    ChessMove possibleMove4 = new ChessMove(myPosition, newPosition, PieceType.QUEEN);
                    possibleMoves.add(possibleMove4);
                }
            }
            // Move diagonal one if a piece can be captured
            if (currentRow > 2 && currentCol > 1 && currentCol < 8 && pieceColor == ChessGame.TeamColor.BLACK) {
                ChessPosition newPosition1 = new ChessPosition(currentRow - 1, currentCol - 1);
                ChessPosition newPosition2 = new ChessPosition(currentRow - 1, currentCol + 1);
                if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition1, null);
                    possibleMoves.add(possibleMove);
                }
                if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != pieceColor) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition2, null);
                    possibleMoves.add(possibleMove);
                }
            }
            if (currentRow == 2 && currentCol > 1 && currentCol < 8 && pieceColor == ChessGame.TeamColor.BLACK) {
                ChessPosition newPosition1 = new ChessPosition(currentRow - 1, currentCol - 1);
                ChessPosition newPosition2 = new ChessPosition(currentRow - 1, currentCol + 1);
                if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != pieceColor) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition1, PieceType.BISHOP);
                    possibleMoves.add(possibleMove1);
                    ChessMove possibleMove2 = new ChessMove(myPosition, newPosition1, PieceType.KNIGHT);
                    possibleMoves.add(possibleMove2);
                    ChessMove possibleMove3 = new ChessMove(myPosition, newPosition1, PieceType.ROOK);
                    possibleMoves.add(possibleMove3);
                    ChessMove possibleMove4 = new ChessMove(myPosition, newPosition1, PieceType.QUEEN);
                    possibleMoves.add(possibleMove4);
                }
                if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != pieceColor) {
                    ChessMove possibleMove1 = new ChessMove(myPosition, newPosition2, PieceType.BISHOP);
                    possibleMoves.add(possibleMove1);
                    ChessMove possibleMove2 = new ChessMove(myPosition, newPosition2, PieceType.KNIGHT);
                    possibleMoves.add(possibleMove2);
                    ChessMove possibleMove3 = new ChessMove(myPosition, newPosition2, PieceType.ROOK);
                    possibleMoves.add(possibleMove3);
                    ChessMove possibleMove4 = new ChessMove(myPosition, newPosition2, PieceType.QUEEN);
                    possibleMoves.add(possibleMove4);
                }
            }
        }
        if (type == PieceType.QUEEN || type == PieceType.ROOK) {
            // Move up
            while (currentRow < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
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
            }
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
            // Move right
            while (currentCol < 8) {
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol + 1);
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
                currentCol++;
            }
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
            // Move down
            while (currentRow > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
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
            }
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
            // Move left
            while (currentCol > 1) {
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol - 1);
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
                currentCol--;
            }
            currentRow = myPosition.getRow();
            currentCol = myPosition.getColumn();
        }
        // instead of "else if queen" logic here I want to try to put it as an or in rook / bishop moves since the
        // queen is basically just a rook and a bishop.
        return possibleMoves;
    }

}
