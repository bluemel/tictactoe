package de.martinbluemel.tictactoe.presentation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

public class SettingsDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel panelCenter = new JPanel();
    private JPanel panelSouth = new JPanel();
    private JLabel numInLineLabel = new JLabel("Number of stones in line:");
    private JComboBox numInLineBox = new JComboBox();
    private JLabel dimLabelX = new JLabel("Field width:");
    private JTextField dimFieldX = new JTextField();
    private JLabel dimLabelY = new JLabel("Field height:");
    private JTextField dimFieldY = new JTextField();
    private JButton buttonOk = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancel");

    private TicTacToeSwing ui = null;
    private static final int[] NUMS_IN_LINE = { 3, 4, 5 };
    private int numInLine;
    private int dimX = 3;
    private int dimY = 3;

    public SettingsDialog(final TicTacToeSwing ui) {
        this.ui = ui;
        this.setTitle("Tic Tac Toe Settings");
        this.setSize(200, 150);
        this.panelCenter.setLayout(new GridBagLayout());
        this.panelSouth.setLayout(new GridBagLayout());
        this.numInLineBox.setModel(new ComboBoxModel() {
            @Override
            public int getSize() {
                return NUMS_IN_LINE.length;
            }
            @Override
            public Object getElementAt(int index) {
                // auto boxing
                return NUMS_IN_LINE[index];
            }
            @Override
            public void setSelectedItem(Object item) {
                numInLine = ((Integer) item).intValue();
            }
            @Override
            public Object getSelectedItem() {
                // auto boxing
                return numInLine;
            }
            @Override
            public void addListDataListener(ListDataListener l) {
            }            
            @Override
            public void removeListDataListener(ListDataListener l) {
            }
        });
        this.numInLineBox.setRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                return new JLabel(((Integer) value).toString());
            }
        });
        this.panelCenter.add(numInLineLabel, new GridBagConstraints(0, 0, 1, 1,
                0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.panelCenter.add(numInLineBox, new GridBagConstraints(1, 0, 1, 1,
                1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.panelCenter.add(dimLabelX, new GridBagConstraints(0, 1, 1, 1,
                0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.panelCenter.add(dimFieldX, new GridBagConstraints(1, 1, 1, 1,
                1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.panelCenter.add(dimLabelY, new GridBagConstraints(0, 2, 1, 1,
                0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.panelCenter.add(dimFieldY, new GridBagConstraints(1, 2, 1, 1,
                1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.getContentPane().add(panelCenter, BorderLayout.CENTER);
        this.buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        this.buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        this.panelSouth.add(buttonOk, new GridBagConstraints(0, 0, 1, 1,
                1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.panelSouth.add(buttonCancel, new GridBagConstraints(1, 0, 1, 1,
                1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0
                ));
        this.update();
        this.getContentPane().add(panelSouth, BorderLayout.SOUTH);
        this.setModal(true);
    }

    private void update() {
        this.numInLine = this.ui.getGame().getNumInLine();
        this.dimX = this.ui.getGame().getField().getDimX();
        this.dimY = this.ui.getGame().getField().getDimY();
        this.dimFieldX.setText(Integer.toString(this.dimX));
        this.dimFieldY.setText(Integer.toString(this.dimY));
    }

    private void ok() {
        this.numInLine = (Integer) this.numInLineBox.getSelectedItem();
        this.dimX = Integer.parseInt(this.dimFieldX.getText());
        this.dimY = Integer.parseInt(this.dimFieldY.getText());
        final boolean changedNum = this.numInLine != this.ui.getGame().getNumInLine();
        final boolean changedDim = this.dimX != this.ui.getGame().getField().getDimX()
            || this.dimY != this.ui.getGame().getField().getDimY();
        if (changedNum || changedDim) {
            try {
                this.validateInput();
                this.ui.getGame().setNumInLine(this.numInLine);
                this.ui.getGame().setDim(new Dimension(this.dimX, this.dimY));
                this.setVisible(false);
            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Tic Tac Toe ERROR", JOptionPane.ERROR_MESSAGE);
                update();
            }
        } else {
            this.setVisible(false);
        }
    }

    private void validateInput() {
        if (this.numInLine > this.dimX) {
            throw new ValidationException("Numbers of stones in line ("
                    + this.numInLine + ")\nmust not be greater than field width ("
                    + this.dimX);
        }
        if (this.numInLine > this.dimY) {
            throw new ValidationException("Numbers of stones in line ("
                    + this.numInLine + ")\nmust not be greater than field height ("
                    + this.dimY);
        }
        if (this.dimX < 3) {
            throw new ValidationException("Field width must not be less than 3");
        }
        if (this.dimY < 3) {
            throw new ValidationException("Field height must not be less than 3");
        }
        if (this.dimX > 50) {
            throw new ValidationException("Field width must not be greater than 50");
        }
        if (this.dimY > 50) {
            throw new ValidationException("Field height must not be greater than 50");
        }
    }

    private void cancel() {
        this.setVisible(false);
    }
}
