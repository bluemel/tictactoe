package de.martinbluemel.tictactoe.presentation;

import de.martinbluemel.tictactoe.domain.TicTacToe.GameState;

public interface UserInterface {
    void messageInfo(String message);
    void messageError(String message);
    boolean askYesNo(String message);
    void updateUI();
    void stateChanged(GameState state);
}
