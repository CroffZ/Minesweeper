package cn.croff.view;

import cn.croff.controller.GameController;
import cn.croff.model.GameModel;

import javax.swing.*;
import java.awt.*;

/**
 * The class GameView provides the current view of the entire Game.
 * It extends JFrame and lays out a matrix of DotButton (the actual game) and two instances of JButton.
 * The action listener for the buttons is the controller.
 */
public class GameView extends JFrame {

    private DotButton[][] board;
    private GameModel gameModel;
    private JLabel stepsLabel;
    private JLabel flagsAndMinesLabel;

    /**
     * Constructor used for initializing the Frame.
     *
     * @param gameModel      the model of the game (already initialized)
     * @param gameController the controller
     */
    public GameView(GameModel gameModel, GameController gameController) {
        super("Minesweeper");
        this.gameModel = gameModel;
        int width = gameModel.getWidth();
        int height = gameModel.getHeight();

        JPanel root = new JPanel(new BorderLayout());

        JPanel top = new JPanel();
        flagsAndMinesLabel = new JLabel();
        top.add(flagsAndMinesLabel);
        root.add(top, BorderLayout.NORTH);

        JPanel bottom = new JPanel();
        stepsLabel = new JLabel();
        bottom.add(stepsLabel);
        JButton reset = new JButton("Reset");
        reset.addActionListener(gameController);
        bottom.add(reset);
        JButton quit = new JButton("Quit");
        quit.addActionListener(gameController);
        bottom.add(quit);
        root.add(bottom, BorderLayout.SOUTH);

        JPanel content = new JPanel(new GridLayout(height, width));
        board = new DotButton[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                DotButton button = new DotButton(j, i, getIcon(j, i));
                button.addMouseListener(gameController);
                button.addActionListener(gameController);
                content.add(button);
                board[i][j] = button;
            }
        }
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(gameModel.getWidth() * 28, gameModel.getHeight() * 28 + 90);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        update();
    }

    /**
     * Update the status of the board's DotButton instances based on the current game model, then redraws the view.
     */
    public void update() {
        stepsLabel.setText("Number of steps: " + gameModel.getNumberOfSteps());
        flagsAndMinesLabel.setText("Number of mines: " + gameModel.getNumberOfMines() + " | Number of flags: " + gameModel.getNumberOfFlags());
        for (int i = 0; i < gameModel.getHeight(); i++) {
            for (int j = 0; j < gameModel.getWidth(); j++) {
                board[i][j].setIconNumber(getIcon(j, i));
            }
        }
        this.repaint();
    }

    /**
     * Returns the icon value that must be used for a given dot in the game.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the icon to use for the dot at location (i,j)
     */
    private int getIcon(int i, int j) {
        if (gameModel.isFlag(i, j)) {
            return DotButton.FLAGGED;
        } else if (gameModel.isCovered(i, j)) {
            return DotButton.COVERED;
        } else if (gameModel.isMined(i, j)) {
            if (gameModel.hasBeenClicked(i, j)) {
                return DotButton.CLICKED_MINE;
            } else {
                return DotButton.MINED;
            }
        } else {
            return gameModel.getNeighbouringMines(i, j);
        }
    }
}
