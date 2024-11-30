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
        switch (col) {
            case 1 -> {
                return "A" + row;
            }
            case 2 -> {
                return "B" + row;
            }
            case 3 -> {
                return "C" + row;
            }
            case 4 -> {
                return "D" + row;
            }
            case 5 -> {
                return "E" + row;
            }
            case 6 -> {
                return "F" + row;
            }
            case 7 -> {
                return "G" + row;
            }
            case 8 -> {
                return "H" + row;
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
