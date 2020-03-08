package de.martinbluemel.tictactoe.domain;

/**
 * A simple player.
 *
 * @author Martin Bluemel
 */
public class Player {

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    private String name;

    public String getName() {
        if (this.name.length() == 0) {
            return null;
        }
        return name;
    }

    private char symbol;

    public char getSymbol() {
        return symbol;
    }

    public Player(final String name, final char symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
