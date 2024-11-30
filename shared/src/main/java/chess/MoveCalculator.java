package chess;

import java.util.ArrayList;

public class MoveCalculator {
    ChessBoard board;
    ChessPosition myPosition;
    int currentRow;
    int currentCol;
    ChessPiece.PieceType type;
    ChessGame.TeamColor color;

   public MoveCalculator (ChessBoard board, ChessPosition myPosition, int currentRow, int currentCol,
                          ChessPiece.PieceType type, ChessGame.TeamColor color) {
       this.board = board;
       this.myPosition = myPosition;
       this.currentRow = currentRow;
       this.currentCol = currentCol;
       this.type = type;
       this.color = color;
   }

   public ArrayList<ChessMove> calculateInfiniteDiagonalMoves() {
       ArrayList<ChessMove> possibleMoves = new ArrayList<>();

       // Move up and to the right
       while (currentRow < 8 && currentCol < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol + 1);
           if (board.getPiece(newPosition) == null) {
               ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
               possibleMoves.add(possibleMove);
           }
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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

       return possibleMoves;
   }

   public ArrayList<ChessMove> calculateInfiniteStraightMoves() {
       ArrayList<ChessMove> possibleMoves = new ArrayList<>();

       // Move up
       while (currentRow < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
           if (board.getPiece(newPosition) == null) {
               ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
               possibleMoves.add(possibleMove);
           }
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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
           else if (color != board.getPiece(newPosition).getTeamColor()) {
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
       return possibleMoves;
   }

   public ArrayList<ChessMove> calculateKingMoves() {
       ArrayList<ChessMove> possibleMoves = new ArrayList<>();

       // Move up
       if (currentRow < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move up and right
       if (currentRow < 8 && currentCol < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol + 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move right
       if (currentCol < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow, currentCol + 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move down and right
       if (currentRow > 1 && currentCol < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol + 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move down
       if (currentRow > 1) {
           ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move down and left
       if (currentRow > 1 && currentCol > 1) {
           ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol - 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move left
       if (currentCol > 1) {
           ChessPosition newPosition = new ChessPosition(currentRow, currentCol - 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move left and up
       if (currentRow < 8 && currentCol > 1) {
           ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol - 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       return possibleMoves;
   }

   public ArrayList<ChessMove> calculateKnightMoves() {
       ArrayList<ChessMove> possibleMoves = new ArrayList<>();

       // Move up 2 and right 1
       if (currentRow < 7 && currentCol < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow + 2, currentCol + 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move up 1 and right 2
       if (currentRow < 8 && currentCol < 7) {
           ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol + 2);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move down 1 and right 2
       if (currentRow > 1 && currentCol < 7) {
           ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol + 2);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move down 2 and right 1
       if (currentRow > 2 && currentCol < 8) {
           ChessPosition newPosition = new ChessPosition(currentRow - 2, currentCol + 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move down 2 and left 1
       if (currentRow > 2 && currentCol > 1) {
           ChessPosition newPosition = new ChessPosition(currentRow - 2, currentCol - 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move down 1 and left 2
       if (currentRow > 1 && currentCol > 2) {
           ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol - 2);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move up 1 and left 2
       if (currentRow < 8 && currentCol > 2) {
           ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol - 2);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       // Move up 2 and left 1
       if (currentRow < 7 && currentCol > 1) {
           ChessPosition newPosition = new ChessPosition(currentRow + 2, currentCol - 1);
           if (isMovePossible(newPosition)) {
               possibleMoves.add(new ChessMove(myPosition, newPosition, null));
           }
       }
       return possibleMoves;
   }

    public ArrayList<ChessMove> calculateWhitePawnMoves() {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        // White side moves
        // Move forward one or two if it hasn't moved yet
        if (currentRow == 2 && color == ChessGame.TeamColor.WHITE) {
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
        if (currentRow > 2 && currentRow < 7 && color == ChessGame.TeamColor.WHITE) {
            ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
            if (board.getPiece(newPosition) == null) {
                ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        // If we reach the opposite side, include promotion piece
        if (currentRow == 7 && color == ChessGame.TeamColor.WHITE) {
            ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
            if (board.getPiece(newPosition) == null) {
                possibleMoves.addAll(addAllPromotionPiecesForMove(newPosition));
            }
        }
        // Move diagonal one if a piece can be captured
        if (currentRow < 7 && color == ChessGame.TeamColor.WHITE) {
            ChessPosition newPosition1 = null;
            ChessPosition newPosition2 = null;
            if (currentCol > 1) {
                newPosition1 = new ChessPosition(currentRow + 1, currentCol - 1);
            }
            if (currentCol < 8) {
                newPosition2 = new ChessPosition(currentRow + 1, currentCol + 1);
            }
            if (newPosition1 != null) {
                if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != color) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition1, null);
                    possibleMoves.add(possibleMove);
                }
            }
            if (newPosition2 != null) {
                if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != color) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition2, null);
                    possibleMoves.add(possibleMove);
                }
            }
        }
        if (currentRow == 7 && currentCol > 1 && currentCol < 8 && color == ChessGame.TeamColor.WHITE) {
            ChessPosition newPosition1 = new ChessPosition(currentRow + 1, currentCol - 1);
            ChessPosition newPosition2 = new ChessPosition(currentRow + 1, currentCol + 1);
            if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != color) {
                possibleMoves.addAll(addAllPromotionPiecesForMove(newPosition1));
            }
            if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != color) {
                possibleMoves.addAll(addAllPromotionPiecesForMove(newPosition2));
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculateBlackPawnMoves() {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        // Black side moves
        // Move forward(down) one or two if it hasn't moved yet
        if (currentRow == 7 && color == ChessGame.TeamColor.BLACK) {
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
        if (currentRow < 7 && currentRow > 2 && color == ChessGame.TeamColor.BLACK) {
            ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
            if (board.getPiece(newPosition) == null) {
                ChessMove possibleMove = new ChessMove(myPosition, newPosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        // If we reach the opposite side, include promotion piece
        if (currentRow == 2 && color == ChessGame.TeamColor.BLACK) {
            ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
            if (board.getPiece(newPosition) == null) {
                possibleMoves.addAll(addAllPromotionPiecesForMove(newPosition));

            }
        }
        // Move diagonal one if a piece can be captured
        if (currentRow > 2 && color == ChessGame.TeamColor.BLACK) {
            ChessPosition newPosition1 = null;
            ChessPosition newPosition2 = null;
            if (currentCol > 1) {
                newPosition1 = new ChessPosition(currentRow - 1, currentCol - 1);
            }
            if (currentCol < 8) {
                newPosition2 = new ChessPosition(currentRow - 1, currentCol + 1);
            }
            if (newPosition1 != null) {
                if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != color) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition1, null);
                    possibleMoves.add(possibleMove);
                }
            }
            if (newPosition2 != null) {
                if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != color) {
                    ChessMove possibleMove = new ChessMove(myPosition, newPosition2, null);
                    possibleMoves.add(possibleMove);
                }
            }
        }
        if (currentRow == 2 && currentCol > 1 && currentCol < 8 && color == ChessGame.TeamColor.BLACK) {
            ChessPosition newPosition1 = new ChessPosition(currentRow - 1, currentCol - 1);
            ChessPosition newPosition2 = new ChessPosition(currentRow - 1, currentCol + 1);
            if (board.getPiece(newPosition1) != null && board.getPiece(newPosition1).getTeamColor() != color) {

                possibleMoves.addAll(addAllPromotionPiecesForMove(newPosition1));
            }
            if (board.getPiece(newPosition2) != null && board.getPiece(newPosition2).getTeamColor() != color) {
                possibleMoves.addAll(addAllPromotionPiecesForMove(newPosition2));
            }
        }
        return possibleMoves;
    }

    public boolean isMovePossible(ChessPosition newPosition) {
        return board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color;
    }

    public ArrayList<ChessMove> addAllPromotionPiecesForMove(ChessPosition newPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
        return possibleMoves;
    }
}
