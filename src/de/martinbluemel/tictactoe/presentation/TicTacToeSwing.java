package de.martinbluemel.tictactoe.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListDataListener;

import de.martinbluemel.tictactoe.domain.IllegalMoveException;
import de.martinbluemel.tictactoe.domain.Move;
import de.martinbluemel.tictactoe.domain.Player;
import de.martinbluemel.tictactoe.domain.PlayingFieldCell;
import de.martinbluemel.tictactoe.domain.TicTacToe;
import de.martinbluemel.tictactoe.domain.TicTacToe.GameState;

public class TicTacToeSwing implements UserInterface {

    private JFrame frame = new JFrame();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu submenuGame = new JMenu("Game");
    private JMenuItem menuItemNew = new JMenuItem("New");
    private JMenuItem menuItemSettings = new JMenuItem("Settings...");
    private JMenuItem menuItemClose = new JMenuItem("Close");

    private JPanel playingField = new JPanel();
    private JPanel south = new JPanel();
    private JLabel player1Label = new JLabel();
    private JComboBox<ImageIcon> player1Icon = new JComboBox<ImageIcon>();
    private Border raisedBorder = new EtchedBorder(EtchedBorder.RAISED);
    private JTextField player1Field = new JTextField();
    private JLabel player2Label = new JLabel();
    private JComboBox<ImageIcon> player2Icon = new JComboBox<ImageIcon>();
    private JTextField player2Field = new JTextField();
    private JTextField messageField = new JTextField();
    private JButton newButton = new JButton("New Match");
    private JButton closeButton = new JButton("Close");

    // field buttons
    private JButton[][] buttons;
    private Color bgColorNormal = null;
    private Color bgColorWinner = Color.yellow;

    // player icons
    private ImageIcon icon1 = new ImageIcon(ClassLoader.getSystemResource("de/martinbluemel/tictactoe/presentation/player1Icon.png"));
    private ImageIcon icon2 = new ImageIcon(ClassLoader.getSystemResource("de/martinbluemel/tictactoe/presentation/player2Icon.png"));
    // combo box model
    private ImageIcon[] playerIcons = {
            this.icon1,
            this.icon2
    };
    // combo box renderer components
    private JButton[] playerIconsComponents = {
            new JButton(this.icon1),
            new JButton(this.icon2)
    };

    // the game
    private TicTacToe game = null;

    // settings
    private ImageIcon iconPlayer1 = this.icon1;
    private ImageIcon iconPlayer2 = this.icon2;
    private int dimX;
    private int dimY;

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new TicTacToeSwing().playTheGame();
    }

    public TicTacToeSwing() {
        this.game = new TicTacToe(this);
        this.frame.setSize(400, 600);
        this.frame.setTitle("Tic Tac Toe App");
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        this.menuItemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playNewMatch();
            }
        });
        this.menuItemSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSettings();
            }
        });
        this.menuItemClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        this.submenuGame.add(this.menuItemNew);
        this.submenuGame.add(this.menuItemSettings);
        this.submenuGame.add(this.menuItemClose);
        this.menuBar.add(this.submenuGame);
        this.frame.getContentPane().add(this.menuBar, BorderLayout.NORTH);
        refreshButtons();
        this.south.setLayout(new GridBagLayout());
        this.player1Label.setText("Player 1");
        this.player1Icon.setModel(new ComboBoxModel<ImageIcon>() {
            @Override
            public void addListDataListener(final ListDataListener l) {
            }
            @Override
            public void removeListDataListener(final ListDataListener l) {
            }
            @Override
            public int getSize() {
                return playerIcons.length;
            }
            @Override
            public ImageIcon getElementAt(final int index) {
                return playerIcons[index];
            }
            @Override
            public void setSelectedItem(final Object anItem) {
                if (anItem == icon1) {
                    iconPlayer1 = icon1;
                    iconPlayer2 = icon2;
                    player2Icon.updateUI();
                } else {
                    iconPlayer1 = icon2;
                    iconPlayer2 = icon1;                    
                    player2Icon.updateUI();
                }
            }
            @Override
            public Object getSelectedItem() {
                return iconPlayer1;
            }
        });
        this.player1Icon.setRenderer(new ListCellRenderer<ImageIcon>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends ImageIcon> list, ImageIcon value, int index,
					boolean isSelected, boolean cellHasFocus) {
                if (value == icon1) {
                    return playerIconsComponents[0];
                } else {
                    return playerIconsComponents[1];
                }
			}
        });
        this.player1Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (game.getState() == GameState.newgame) {
                    game.setInitialPlayer(game.getPlayer1());
                    playNewMatch();
                }
            }
        });
        this.player2Label.setText("Player 2");
        this.player2Icon.setModel(new ComboBoxModel<ImageIcon>() {
            @Override
            public void addListDataListener(final ListDataListener l) {
            }
            @Override
            public void removeListDataListener(final ListDataListener l) {
            }
            @Override
            public int getSize() {
                return playerIcons.length;
            }
            @Override
            public ImageIcon getElementAt(final int index) {
                return playerIcons[index];
            }
            @Override
            public void setSelectedItem(final Object anItem) {
                if (anItem == icon1) {
                    iconPlayer2 = icon1;
                    iconPlayer1 = icon2;
                    player1Icon.updateUI();
                } else {
                    iconPlayer2 = icon2;
                    iconPlayer1 = icon1;
                    player1Icon.updateUI();
                }
            }
            @Override
            public Object getSelectedItem() {
                return iconPlayer2;
            }
        });
        this.player2Icon.setRenderer(new ListCellRenderer<ImageIcon>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends ImageIcon> list, ImageIcon value, int index,
					boolean isSelected, boolean cellHasFocus) {
                if (value == icon1) {
                    return playerIconsComponents[0];
                } else {
                    return playerIconsComponents[1];
                }
			}
        });
        this.player2Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (game.getState() == GameState.newgame) {
                    game.setInitialPlayer(game.getPlayer2());
                    playNewMatch();
                }
            }
        });
        this.player1Field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                game.getPlayer1().setName(player1Field.getText());
            }
        });
        this.player2Field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                game.getPlayer2().setName(player2Field.getText());
            }
        });
        this.newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playNewMatch();
            }
        });
        this.closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        this.messageField.setEditable(false);
        this.south.add(player1Label, new GridBagConstraints(0, 0, 1, 1,
                0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(player1Icon, new GridBagConstraints(1, 0, 1, 1,
                0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(player1Field, new GridBagConstraints(2, 0, 1, 1,
                1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(player2Label, new GridBagConstraints(0, 1, 1, 1,
                0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(player2Icon, new GridBagConstraints(1, 1, 1, 1,
                0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(player2Field, new GridBagConstraints(2, 1, 1, 1,
                1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(messageField, new GridBagConstraints(0, 2, 3, 1,
                1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(newButton, new GridBagConstraints(0, 3, 1, 1,
                0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));
        this.south.add(closeButton, new GridBagConstraints(2, 3, 1, 1,
                0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        this.frame.getContentPane().add(this.playingField, BorderLayout.CENTER);
        this.frame.getContentPane().add(this.south, BorderLayout.SOUTH);
        this.game.newMatch();
    }

    @Override
    public void messageInfo(final String message) {
        this.messageField.setText(message);
    }

    @Override
    public void messageError(final String message) {
        JOptionPane.showMessageDialog(this.frame, message, "Tic Tac Toe ERROR", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean askYesNo(final String question) {
        final int result = JOptionPane.showOptionDialog(this.frame.getContentPane(),
                question, "Tic Tac Toe Question", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        switch (result) {
        case JOptionPane.YES_OPTION:
            return true;
        default:
            return false;
        }
    }

    @Override
    public void updateUI() {
        if (this.game == null || this.buttons == null || this.buttons[0][0] == null) {
            return;
        }
        if (this.dimX != this.game.getField().getDimX()
                || this.dimY != this.game.getField().getDimY()) {
            refreshButtons();
        }
        for (int row = 0; row < this.dimY; row++) {
            for (int column = 0; column < this.dimX; column++) {
                final JButton button = this.buttons[row][column];
                final PlayingFieldCell cell = this.game.getField().get(row + 1, column + 1);
                final Player player = cell.setBy;
                if (player != null || this.game.winner != null) {
                    button.setEnabled(false);
                    if (player != null) {
                        if (player == this.game.getPlayer1()) {
                            button.setIcon(this.iconPlayer1);
                            button.setDisabledIcon(this.iconPlayer1);
                        } else if (player == this.game.getPlayer2()) {
                            button.setIcon(this.iconPlayer2);
                            button.setDisabledIcon(this.iconPlayer2);
                        } else {
                            throw new AssertionError("invalid player");
                        }
                    } else {
                        button.setIcon(null);
                        button.setDisabledIcon(null);
                    }
                } else {
                    button.setEnabled(true);
                    button.setIcon(null);
                    button.setDisabledIcon(null);
                }
                if (cell.isInWinnerLine) {
                    button.setBackground(this.bgColorWinner);
                } else {
                    button.setBackground(this.bgColorNormal);
                }
            }
        }
        if (this.game.winner == null) {
            if (this.game.getCurrentPlayer() == this.game.getPlayer1()) {
                this.player1Label.setBorder(this.raisedBorder);
                this.player2Label.setBorder(null);
            } else {
                this.player1Label.setBorder(null);
                this.player2Label.setBorder(this.raisedBorder);
            }
        } else {
            this.player1Label.setBorder(null);
            this.player2Label.setBorder(null);
        }
    }

    @Override
    public void stateChanged(final GameState state) {
        if (this.buttons == null || this.buttons[0][0] == null) {
            return;
        }
        updateUI();
        switch (state) {
        case newgame:
        case playing:
            if (!this.buttons[0][0].isEnabled()) {
                enableButtons(true);
            }
            break;
        default:
            if (this.buttons[0][0].isEnabled()) {
                enableButtons(false);
            }
        }
    }

    private void refreshButtons() {
        this.playingField.removeAll();
        this.dimX = this.game.getField().getDimX();
        this.dimY = this.game.getField().getDimY();
        this.buttons = new JButton[this.dimY][this.dimX];
        this.playingField.setLayout(new GridLayout(this.dimY, this.dimX));
        for (int row = 0; row < this.dimY; row++) {
            for (int column = 0; column < this.dimX; column++) {
                final JButton button = new JButton();
                if (this.bgColorNormal == null) {
                    this.bgColorNormal = button.getBackground();
                }
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        moveEntered(e);
                    }
                });
                this.buttons[row][column] = button;
                this.playingField.add(button, row * dimX + column);
            }
        }
        int width = this.frame.getWidth();
        int height = this.frame.getHeight();
        this.frame.setSize(0, 0);
        this.frame.setSize(width, height);
        this.frame.repaint();
    }

    public TicTacToe getGame() {
        return game;
    }

    public void setGame(TicTacToe game) {
        this.game = game;
    }

    private void playTheGame() {
        this.frame.setVisible(true);
    }

    private void playNewMatch() {
        boolean newMatch = true;
        if (this.game.getState() == GameState.playing) {
            newMatch = askYesNo("Do you really want to interrupt the running game");
        }
        if (newMatch) {
            this.game.newMatch();
            this.messageInfo("");
        } else {
            this.messageInfo("aborted interrupt");
        }
    }

    private void editSettings() {
        new SettingsDialog(this).setVisible(true);        
    }

    private void close() {
        boolean end = true;
        if (this.game.getState() == GameState.playing) {
            end = askYesNo("Do you really want to finish the running game");
        }
        if (end) {
            System.exit(0);
        } else {
            messageInfo("closing aborted");
        }
    }

    private void enableButtons(final boolean b) {
        for (int i = 0; i < this.dimY; i++) {
            for (int j = 0; j < this.dimY; j++) {
                this.buttons[i][j].setEnabled(b);
            }
        }
    }

    private void moveEntered(final ActionEvent e) {
        final Dimension indexes = this.getIndexes((JButton) e.getSource());
        final Move move = new Move(this.game.getCurrentPlayer(), indexes.width + 1, indexes.height + 1);
        try {
           this.game.moveEntered(move);
        } catch (IllegalMoveException e1) {
           JOptionPane.showMessageDialog(this.frame, e1.getMessage(), "Tic Tac Toe ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Dimension getIndexes(final JButton button) {
        for (int i = 0; i < this.dimY; i++) {
            for (int j = 0; j < this.dimX; j++) {
                if (button == this.buttons[i][j]) {
                    return new Dimension(i, j);
                }
            }
        }
        return null;
    }
}
