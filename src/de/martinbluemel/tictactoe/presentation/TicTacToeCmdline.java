package de.martinbluemel.tictactoe.presentation;

import java.io.InputStream;

import de.martinbluemel.tictactoe.domain.Move;
import de.martinbluemel.tictactoe.domain.Player;
import de.martinbluemel.tictactoe.domain.PlayingField;
import de.martinbluemel.tictactoe.domain.TicTacToe;
import de.martinbluemel.tictactoe.domain.TicTacToe.GameState;

/**
 * Play the game on the command line.
 *
 * @author Martin Buemel
 */
public class TicTacToeCmdline implements UserInterface {

    private TicTacToe game = null;

    public static void main(String[] args) {
        new TicTacToeCmdline().playTheGame();
    }

    public void playTheGame() {
        this.game = new TicTacToe(this);
        do {
            this.game.newMatch();
            Move move = enterMove(this.game, this.game.getCurrentPlayer());
            while ((!move.cancel) && this.game.winner == null
                    && (!this.game.getField().isSetAll())) {
                move = enterMove(this.game, this.game.getCurrentPlayer());
            }
            if (move.cancel) {
                messageInfo("The match has been cancelled.");
            }
        } while (promptYesNo("Do you want to play again?", true));
    }

    @Override
    public void messageInfo(final String message) {
        System.out.println(message);
    }

    @Override
    public void messageError(final String message) {
        System.out.println(message);
    }

    @Override
    public void updateUI() {
        System.out.println("-------");
        final int dimX = this.game.getField().getDimX();
        final int dimY = this.game.getField().getDimY();
        for (int row = 1; row <= dimY; row++) {
            for (int column = 1; column <= dimX; column++) {
                if (this.game.getField().get(row, column).setBy == null) {
                    System.out.print("|" + PlayingField.EMPTY);
                } else {
                    System.out.print("|" + this.game.getField().get(row, column).setBy.getSymbol());
                }
            }
            System.out.println("|");
        }
        System.out.println("-------");
    }

    @Override
    public void stateChanged(GameState state) {
    }

    private Move enterMove(final TicTacToe game, final Player player) {
        Move move = null;
        while (move == null) {
            final String eingabe =
                enterValue(player.getName() + ": ", "cancel");
            if (eingabe.equals("cancel")) {
//                this.game.moveEntered(new Move(player, true));
                return new Move(player, true);
            }
            try {
                int x = Integer.parseInt(eingabe.substring(0, 1));
                int y = Integer.parseInt(eingabe.substring(1, 2));
                move = new Move(player, x, y);
                    game.getField().checkSet(move.row, move.column);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Ungueltiger Zug zu index: " + e.getMessage());
                move = null;
            } catch (NumberFormatException e) {
                System.out.println("Ungueltige eingabe: \"" + eingabe + "\"");
                move = null;
            } catch (RuntimeException e) {
                System.out.println("Ungueltige eingabe: \"" + eingabe + "\": field is already set.");
                move = null;
            }
        }
        this.game.moveEntered(move);
        return move;
    }

    private static boolean promptYesNo(
            final String question, final boolean defaultValue) {
        boolean ret = defaultValue;
        String defaultValString = "N";
        if (defaultValue) {
            defaultValString = "Y";
        }
        System.out.print(question + " <Y|N> [" + defaultValString + "] >");
        final String answer = read(System.in);
        if (answer.equalsIgnoreCase("Y")) {
            ret = true;
        } else if (answer.equalsIgnoreCase("N")) {
            ret = false;
        }
        return ret;
    }

    public String enterValue(final String msg, final String defaultValue) {
        final StringBuffer prompt = new StringBuffer(msg);
        if (defaultValue != null) {
            prompt.append(" [");
            prompt.append(defaultValue);
            prompt.append("]");
        }
        prompt.append(" >");
        System.out.print(prompt.toString());
        String enteredValue = read(System.in);
        if (enteredValue.length() == 0) {
            enteredValue = defaultValue;
        }
        return enteredValue;
    }

    private static String read(final InputStream in) {
        String line = null;
        try {
            java.io.BufferedReader stdin =
                new java.io.BufferedReader(
                        new java.io.InputStreamReader(in));
            line = stdin.readLine();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }

    @Override
    public boolean askYesNo(final String question) {
        return promptYesNo(question, false);
    }
}
