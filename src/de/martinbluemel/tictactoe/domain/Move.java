package de.martinbluemel.tictactoe.domain;

/**
 * Command object for a player's move.
 *
 * @author Martin Blumel
 */
public class Move {

    public Player player;
    public int row;
    public int column;
    public boolean cancel = false;

    public Move(final Player spieler, final int zeile, final int spalte) {
        this.player = spieler;
        this.row = zeile;
        this.column = spalte;
    }
    public Move(final Player spieler, final boolean cancel) {
        this.player = spieler;
        this.cancel = true;
    }
}
