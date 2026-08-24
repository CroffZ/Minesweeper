package cn.croff.minesweeper.view;

import cn.croff.minesweeper.model.GameModel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

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

        JPanel root = new JPanel(new BorderLayout(0, 6));
        root.add(createStatusPanel(), BorderLayout.NORTH);
        root.add(new JScrollPane(createBoard(actionListener, mouseListener)), BorderLayout.CENTER);
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
        statusLabel.setText("Mines: " + model.getMineCount() + "   Flags: " + model.getFlagCount());
        movesLabel.setText("Moves: " + model.getMoveCount());
        for (int y = 0; y < model.getHeight(); y++) {
            for (int x = 0; x < model.getWidth(); x++) {
                board[y][x].display(model.getCellState(x, y), model.getAdjacentMines(x, y));
            }
        }
        repaint();
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.add(statusLabel);
        return panel;
    }

    private JPanel createBoard(ActionListener actionListener, MouseListener mouseListener) {
        JPanel panel = new JPanel(new GridLayout(model.getHeight(), model.getWidth()));
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
        JPanel panel = new JPanel();
        panel.add(movesLabel);
        panel.add(createButton("Reset", RESET_COMMAND, actionListener));
        panel.add(createButton("Quit", QUIT_COMMAND, actionListener));
        return panel;
    }

    private JButton createButton(String text, String command, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(actionListener);
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
