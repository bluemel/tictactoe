package de.martinbluemel.tictactoe.domain;

import java.awt.Dimension;

/**
 * The playing field contains a 2 dimensional character array
 * set to EMPTY or to the player's symbol.
 *
 * @author Martin Bluemel
 */
public class PlayingField {

    public final static char EMPTY = ' ';

    // Dimensions
    private int dimX = 3;
    private int dimY = 3;

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    protected void setDim(Dimension dim) {
        this.dimY = dim.height;
        this.dimX = dim.width;
        this.array = new PlayingFieldCell[this.dimY][this.dimX];
        clear();
    }

    private PlayingFieldCell[][] array = new PlayingFieldCell[this.dimY][this.dimX];

    public PlayingField() {
        clear();
    }

    public void checkSet(final int row, final int column) {
        if (row < 1 || row > this.dimY) {
            throw new IllegalMoveException(new ArrayIndexOutOfBoundsException(row));
        }
        if (column < 1 || column > this.dimX) {
            throw new IllegalMoveException(new ArrayIndexOutOfBoundsException(column));
        }
        if (isSet(row, column)) {
            throw new IllegalMoveException("field [" + row + "|" + column + "]already set");
        }
    }

    public void set(int row, int column, final Player player) {
        this.array[row - 1][column - 1].setBy = player;
    }

    public PlayingFieldCell get(int row, int column) {
        return this.array[row - 1][column - 1];
    }

    public boolean isSet(int row, int column) {
        if (array[row - 1][column - 1].setBy == null) {
            return false;
        }
        return true;
    }

    public boolean isSetAll() {
        for (int y = 0; y < this.dimY; y++) {
            for (int x = 0; x < this.dimX; x++) {
                if (this.array[y][x].setBy == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clear() {
        for (int y = 0; y < this.dimY; y++) {
            for (int x = 0; x < this.dimX; x++) {
                this.array[y][x] = new PlayingFieldCell();
            }
        }
    }
}
