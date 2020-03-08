package de.martinbluemel.tictactoe.domain;

import java.awt.Dimension;

import de.martinbluemel.tictactoe.presentation.UserInterface;

/**
 * The game's core business logic.
 *
 * @author Martin Bluemel
 */
public class TicTacToe {

    public enum GameState {
        initializing, newgame, playing, finishedWithWinner, finishedStandOff
    }

    public enum Direction {
        n, no, o, so, s, sw, w, nw
    }

    private Player player1 = new Player("Player 1", 'x');

    public Player getPlayer1() {
        return player1;
    }

    private Player player2 = new Player("Player 2", 'o');

    public Player getPlayer2() {
        return player2;
    }

    private PlayingField field = new PlayingField();

    public PlayingField getField() {
        return field;
    }

    private UserInterface ui;

    private GameState state = GameState.initializing;

    public GameState getState() {
        return state;
    }

    private Player initialPlayer = player1;

    private Player currentPlayer = null;

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Player winner = null;

    public TicTacToe(final UserInterface ui) {
        this.ui = ui;
    }

    private int numInLine = 3;

    public int getNumInLine() {
        return numInLine;
    }

    public void setNumInLine(int numInLine) {
        this.numInLine = numInLine;
    }

    private Player getAlternatePlayer(final Player player) {
        if (player == this.player1) {
            return this.player2;
        } else if (player == this.player2) {
            return this.player1;
        } else {
            throw new IllegalArgumentException("neither player 1 or player 2 entered");
        }
    }

    public void newMatch() {
        this.setState(GameState.initializing);
        this.field.clear();
        this.currentPlayer = this.initialPlayer;
        this.winner = null;
        this.setState(GameState.newgame);
        this.ui.updateUI();
    }

    private void setState(final GameState state) {
        this.state = state;
        this.ui.stateChanged(state);
    }

    private boolean inLine(final Player player) {
        final int dimX = this.field.getDimX();
        final int dimY = this.field.getDimY();
        for (int i = 1; i <= dimY; i++) {
            for (int j = 1; j <= dimX; j++) {
                for (int k = 0; k < Direction.values().length; k++) {
                    if (inLineRecursive(i, j, dimY, dimX, player, 1,
                            Direction.values()[k])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean inLineRecursive(int y, int x,
            int dimY, int dimX,
            final Player player, final int depth,
            final Direction direction) {
        final PlayingFieldCell cell = this.field.get(y, x);
        cell.isInWinnerLine = true;
        if (cell.setBy == null || cell.setBy != player) {
            cell.isInWinnerLine = false;
            return false;
        }
        if (depth == this.numInLine) {
            return true;
        } else {
            switch (direction) {
            case n: y--; break;
            case no: y--; x++; break;
            case o: x++; break;
            case so: y++; x++; break;
            case s: y++; break;
            case sw: y++; x--; break;
            case w: x--; break;
            case nw: y--; x--; break;
            }
            if (y >= 1 && y <= dimY && x >= 1 && x <= dimX) {
                final boolean isInLine = inLineRecursive(y, x, dimY, dimX, player, depth + 1, direction);
                cell.isInWinnerLine = isInLine;
                return isInLine;
            } else {
                cell.isInWinnerLine = false;
                return false;
            }
        }
    }

    public Move lastMove = null;

    public void moveEntered(final Move move) {
        switch(this.state) {
        case initializing:
            this.ui.messageError("Game not yet ready.");
            break;
        case finishedWithWinner:
        case finishedStandOff:
            this.ui.messageError("Game already finished.");
            break;
        case newgame:
        case playing:
            try {
                this.field.checkSet(move.row, move.column);
                this.field.set(move.row, move.column, move.player);
                this.lastMove = move;
                if (inLine(this.currentPlayer)) {
                    this.winner = this.currentPlayer;
                    setState(GameState.finishedWithWinner);
                    this.ui.messageInfo(getPlayerName(this.winner) + " has won.");
                } else {
                    if (this.field.isSetAll()) {
                        this.setState(GameState.finishedStandOff);
                        this.ui.messageInfo("Match ended stand-off.");
                    } else {
                        this.currentPlayer = getAlternatePlayer(this.currentPlayer);
                    }
                }
                if (this.state == GameState.newgame) {
                    this.state = GameState.playing;
                }
                this.ui.updateUI();
            } catch (IllegalMoveException e1) {
                this.ui.messageError(e1.getMessage());
            }
            break;
        }
    }

    public String getPlayerName(final Player player) {
        if (player.getName() == null) {
            if (player == this.player1) {
                return "Player 1";
            } else if (player == this.player2) {
                return "Player 2";
            }
            throw new IllegalArgumentException("Neither player 1 nor player 2 entered.");
        }
        return player.getName();
    }

    public void setInitialPlayer(final Player player) {
        this.initialPlayer = player;
    }

    public void setDim(Dimension dim) {
        this.field.setDim(dim);
        this.ui.updateUI();
    }
}
