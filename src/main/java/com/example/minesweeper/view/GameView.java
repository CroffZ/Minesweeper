package com.example.minesweeper.view;

import com.example.minesweeper.model.GameModel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;

/**
 * Renders the current model using Swing components.
 */
public final class GameView extends JFrame {

    public static final String RESET_COMMAND = "reset";
    public static final String QUIT_COMMAND = "quit";

    private static final long serialVersionUID = 1L;

    private final GameModel model;
    private final DotButton[][] board;
    private final JLabel statusLabel;
    private final JLabel movesLabel;

    public GameView(GameModel model, ActionListener actionListener, MouseListener mouseListener) {
        super("Minesweeper");
        this.model = model;
        board = new DotButton[model.getHeight()][model.getWidth()];
        statusLabel = new JLabel();
        movesLabel = new JLabel();

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(new Color(241, 245, 249));
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 16, 18));
        root.add(createStatusPanel(), BorderLayout.NORTH);
        JScrollPane boardScrollPane = new JScrollPane(createBoard(actionListener, mouseListener));
        boardScrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        boardScrollPane.getViewport().setBackground(new Color(203, 213, 225));
        root.add(boardScrollPane, BorderLayout.CENTER);
        root.add(createControlPanel(actionListener), BorderLayout.SOUTH);

        setContentPane(root);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        refresh();
        pack();
        fitToScreen();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refresh() {
        statusLabel.setText("MINES  " + model.getMineCount() + "   •   FLAGS  " + model.getFlagCount());
        movesLabel.setText("Moves: " + model.getMoveCount());
        for (int y = 0; y < model.getHeight(); y++) {
            for (int x = 0; x < model.getWidth(); x++) {
                board[y][x].display(model.getCellState(x, y), model.getAdjacentMines(x, y));
            }
        }
        repaint();
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 6));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("MINESWEEPER");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitleLabel = new JLabel("Clear the board. Avoid the mines.");
        subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(100, 116, 139));

        JPanel titleBlock = new JPanel(new BorderLayout(0, 2));
        titleBlock.setOpaque(false);
        titleBlock.add(titleLabel, BorderLayout.NORTH);
        titleBlock.add(subtitleLabel, BorderLayout.SOUTH);

        statusLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        statusLabel.setForeground(new Color(30, 64, 175));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(239, 246, 255));

        panel.add(titleBlock, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createBoard(ActionListener actionListener, MouseListener mouseListener) {
        JPanel panel = new JPanel(new GridLayout(model.getHeight(), model.getWidth()));
        panel.setBackground(new Color(148, 163, 184));
        for (int y = 0; y < model.getHeight(); y++) {
            for (int x = 0; x < model.getWidth(); x++) {
                DotButton button = new DotButton(x, y);
                button.addActionListener(actionListener);
                button.addMouseListener(mouseListener);
                board[y][x] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    private JPanel createControlPanel(ActionListener actionListener) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(movesLabel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(createButton("New game", RESET_COMMAND, actionListener, true));
        buttons.add(createButton("Quit", QUIT_COMMAND, actionListener, false));
        panel.add(buttons, BorderLayout.EAST);

        movesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        movesLabel.setForeground(new Color(71, 85, 105));
        return panel;
    }

    private JButton createButton(String text, String command, ActionListener actionListener, boolean primary) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(actionListener);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? new Color(37, 99, 235) : new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        button.setForeground(primary ? Color.WHITE : new Color(51, 65, 85));
        button.setBackground(primary ? new Color(37, 99, 235) : Color.WHITE);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(primary ? 104 : 72, 34));
        return button;
    }

    private void fitToScreen() {
        Rectangle usableScreen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        int maximumWidth = Math.max(320, usableScreen.width - 40);
        int maximumHeight = Math.max(240, usableScreen.height - 40);
        setSize(Math.min(getWidth(), maximumWidth), Math.min(getHeight(), maximumHeight));
    }
}
