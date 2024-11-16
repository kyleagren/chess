package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        resetBoard();
    }
    public ChessBoard(ChessBoard board) {
        this.squares = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.squares[i][j] == null) {
                    this.squares[i][j] = null;
                }
                else {
                    this.squares[i][j] = new ChessPiece(board.squares[i][j]);
                }
            }
        }
    }

    public ChessPiece[][] getBoard() {
        return squares;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Gets the position for the king for the specified team. Used to determine if a move would leave the king in check
     * @param color The color (team) of the desired king
     * @return The position that the king is currently located at
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                ChessPiece piece = squares[i][j];
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    return new ChessPosition(i + 1, j + 1);
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Set up white side
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        squares[1][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        squares[1][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        squares[1][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        squares[1][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        squares[1][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        squares[1][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        squares[1][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        squares[1][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        // Set up black side
        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        squares[6][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        squares[6][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        squares[6][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        squares[6][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        squares[6][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        squares[6][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        squares[6][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        squares[6][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
    }

    @Override
    public String toString() {
        StringBuilder newString = new StringBuilder("\n");
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j] != null) {
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.PAWN && squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        newString.append("|P|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.ROOK && squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        newString.append("|R|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.KNIGHT && squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        newString.append("|N|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.BISHOP && squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        newString.append("|B|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.KING && squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        newString.append("|K|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.QUEEN && squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        newString.append("|Q|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.PAWN && squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                        newString.append("|p|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.ROOK && squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                        newString.append("|r|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.KNIGHT && squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                        newString.append("|n|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.BISHOP && squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                        newString.append("|b|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.KING && squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                        newString.append("|k|");
                    }
                    if (squares[i][j].getPieceType() == ChessPiece.PieceType.QUEEN && squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                        newString.append("|q|");
                    }
                }
                else {
                    newString.append("| |");
                }
            }
            newString.append("\n");
        }
        return newString.toString();
    }
}
