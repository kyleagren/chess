package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public String toString() {
        switch (row) {
            case 0 -> {
                return "A" + col;
            }
            case 1 -> {
                return "B" + col;
            }
            case 2 -> {
                return "C" + col;
            }
            case 3 -> {
                return "D" + col;
            }
            case 4 -> {
                return "E" + col;
            }
            case 5 -> {
                return "F" + col;
            }
            case 6 -> {
                return "G" + col;
            }
            case 7 -> {
                return "H" + col;
            }
            default -> {
                return "";
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
