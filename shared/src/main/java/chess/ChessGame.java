package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        TeamColor color = piece.getTeamColor();
        ArrayList<ChessMove> possibleMoves = new ArrayList<>(piece.pieceMoves(board, startPosition));
        for (ChessMove move : possibleMoves) {
            ChessPosition end = move.getEndPosition();
            ChessBoard boardBeforeMoves = new ChessBoard(board);
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && (piece.getTeamColor() == TeamColor.WHITE && end.getRow() == 8 || piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == TeamColor.BLACK && end.getRow() == 1)) {
                board.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            }
            else {
                board.addPiece(end, piece);
            }
            board.addPiece(startPosition, null);

            if (!isInCheck(color)) {
                validMoves.add(move);
            }
            board = new ChessBoard(boardBeforeMoves);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException();
        }
        if (piece.pieceMoves(board, start).isEmpty()) {
            throw new InvalidMoveException();
        }
        // if this move is in validMoves, it makes the move, otherwise it throws an error
        for (ChessMove validMove: validMoves(start)) {
            if (move.equals(validMove)) {
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN && (piece.getTeamColor() == TeamColor.WHITE && end.getRow() == 8 || piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == TeamColor.BLACK && end.getRow() == 1)) {
                    board.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
                }
                else {
                    board.addPiece(end, piece);
                }
                board.addPiece(start, null);
                if (getTeamTurn() == TeamColor.WHITE) {
                    setTeamTurn(TeamColor.BLACK);
                }
                else {
                    setTeamTurn((TeamColor.WHITE));
                };
                return;
            }
        }
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, column));
                if (currentPiece == null) {
                    continue;
                }
                if (currentPiece.getTeamColor() != teamColor) {
                    ArrayList<ChessMove> oppositePieceMoves = new ArrayList<>(currentPiece.pieceMoves(board, new ChessPosition(row, column)));
                    for (ChessMove oppositeMove : oppositePieceMoves) {
                        if (oppositeMove.getEndPosition().equals(board.getKingPosition(teamColor))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        var possibleMoves = new ArrayList<ChessMove>();
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                ChessPosition currentPosition = new ChessPosition(row, column);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece == null) {
                    continue;
                }
                if (currentPiece.getTeamColor() == teamColor) {
                    possibleMoves.addAll(validMoves(currentPosition));
                }
            }
        }
        if (possibleMoves.isEmpty()) {
            if (getTeamTurn() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            }
            else {
                setTeamTurn((TeamColor.WHITE));
            };
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
