package cn.croff.controller;

import cn.croff.model.DotInfo;
import cn.croff.model.GameModel;
import cn.croff.view.DotButton;
import cn.croff.view.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

/**
 * The class <b>GameController</b> is the controller of the game.
 * It is a listener of the view, and has a method <b>play</b> which computes the next step of the game, and updates model and view.
 */
public class GameController implements ActionListener, MouseListener {

    private GameView gameView;
    private GameModel gameModel;

    /**
     * Constructor used for initializing the controller. It creates the game's view and the game's model instances.
     *
     * @param width         the width of the board on which the game will be played
     * @param height        the height of the board on which the game will be played
     * @param numberOfMines the number of mines hidden in the board
     */
    public GameController(int width, int height, int numberOfMines) {
        gameModel = new GameModel(width, height, numberOfMines);
        gameView = new GameView(gameModel, this);
    }

    /**
     * Callback used when the user clicks a button (reset or quit).
     *
     * @param e the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.getClass().equals(DotButton.class)) {
            int x = ((DotButton) source).getColumn();
            int y = ((DotButton) source).getRow();
            play(x, y);
        } else if (source.getClass().equals(JButton.class)) {
            JButton button = (JButton) source;
            if (button.getText().equals("Reset")) {
                reset();
            } else if (button.getText().equals("Quit")) {
                System.exit(0);
            }
        }
    }

    /**
     * Resets the game.
     */
    private void reset() {
        gameModel.reset();
        gameView.update();
    }

    /**
     * This method is called when the user clicks on a square.
     * If that square is not already clicked, then it applies the logic of the game to uncover that square,
     * and possibly end the game if that square was mined, or possibly uncover some other squares.
     * It then checks if the game is finished, and if so, congratulates the player,
     * showing the number of moves, and gives to options: start a new game, or exit.
     *
     * @param width  the selected column
     * @param height the selected line
     */
    private void play(int width, int height) {
        if (gameModel.isCovered(width, height) && !gameModel.isFlag(width, height)) {
            gameModel.step();
            gameModel.click(width, height);
            gameModel.uncover(width, height);
            if (gameModel.isBlank(width, height)) {
                clearZone(gameModel.get(width, height));
            }
            gameView.update();

            if (gameModel.isMined(width, height) || gameModel.isFinished()) {
                gameModel.uncoverAll();
                gameView.update();
                String[] options = {"Play Again", "Quit"};
                int option;
                if (gameModel.isMined(width, height)) {
                    option = JOptionPane.showOptionDialog(null,
                            "Oh, you lost in " + gameModel.getNumberOfSteps() + " steps!\nWould you like to play again?",
                            "Boom!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                } else {
                    option = JOptionPane.showOptionDialog(null,
                            "Congratulations, you won in " + gameModel.getNumberOfSteps() + " steps!\nWould you like to play again?",
                            "Win!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                }
                if (option == JOptionPane.YES_OPTION) {
                    // Play again
                    reset();
                } else {
                    // Exit
                    System.exit(0);
                }
            }
        }
    }

    /**
     * This method computes which new dots should be uncovered
     * when a new square with no mine in its neighborhood has been selected.
     *
     * @param initialDot the DotInfo object corresponding to the selected DotButton that had zero neighbouring mines
     */
    private void clearZone(DotInfo initialDot) {
        Stack<DotInfo> dotStack = new Stack<>();
        dotStack.push(initialDot);
        while (!dotStack.isEmpty()) {
            DotInfo dot = dotStack.pop();
            int x = dot.getX();
            int y = dot.getY();
            if (x > 0 && gameModel.isCovered(x - 1, y) && !gameModel.isMined(x - 1, y)) {
                gameModel.uncover(x - 1, y);
                if (gameModel.isBlank(x - 1, y)) {
                    dotStack.push(gameModel.get(x - 1, y));
                }
            }
            if (x > 0 && y > 0 && gameModel.isCovered(x - 1, y - 1) && !gameModel.isMined(x - 1, y - 1)) {
                gameModel.uncover(x - 1, y - 1);
                if (gameModel.isBlank(x - 1, y - 1)) {
                    dotStack.push(gameModel.get(x - 1, y - 1));
                }
            }
            if (x > 0 && y < gameModel.getHeight() - 1 && gameModel.isCovered(x - 1, y + 1) && !gameModel.isMined(x - 1, y + 1)) {
                gameModel.uncover(x - 1, y + 1);
                if (gameModel.isBlank(x - 1, y + 1)) {
                    dotStack.push(gameModel.get(x - 1, y + 1));
                }
            }
            if (y > 0 && gameModel.isCovered(x, y - 1) && !gameModel.isMined(x, y - 1)) {
                gameModel.uncover(x, y - 1);
                if (gameModel.isBlank(x, y - 1)) {
                    dotStack.push(gameModel.get(x, y - 1));
                }
            }
            if (y < gameModel.getHeight() - 1 && gameModel.isCovered(x, y + 1) && !gameModel.isMined(x, y + 1)) {
                gameModel.uncover(x, y + 1);
                if (gameModel.isBlank(x, y + 1)) {
                    dotStack.push(gameModel.get(x, y + 1));
                }
            }
            if (x < gameModel.getWidth() - 1 && gameModel.isCovered(x + 1, y) && !gameModel.isMined(x + 1, y)) {
                gameModel.uncover(x + 1, y);
                if (gameModel.isBlank(x + 1, y)) {
                    dotStack.push(gameModel.get(x + 1, y));
                }
            }
            if (x < gameModel.getWidth() - 1 && y > 0 && gameModel.isCovered(x + 1, y - 1) && !gameModel.isMined(x + 1, y - 1)) {
                gameModel.uncover(x + 1, y - 1);
                if (gameModel.isBlank(x + 1, y - 1)) {
                    dotStack.push(gameModel.get(x + 1, y - 1));
                }
            }
            if (x < gameModel.getWidth() - 1 && y < gameModel.getHeight() - 1 && gameModel.isCovered(x + 1, y + 1) && !gameModel.isMined(x + 1, y + 1)) {
                gameModel.uncover(x + 1, y + 1);
                if (gameModel.isBlank(x + 1, y + 1)) {
                    dotStack.push(gameModel.get(x + 1, y + 1));
                }
            }
        }
    }

    /**
     * Callback used when the user clicks a button (reset or quit).
     * The right click is processed here.
     *
     * @param e the MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            // Right click for flag
            Object source = e.getSource();
            int x = ((DotButton) source).getColumn();
            int y = ((DotButton) source).getRow();
            if (gameModel.isCovered(x, y) || gameModel.isFlag(x, y)) {
                gameModel.setFlag(x, y, !gameModel.isFlag(x, y));
                gameView.update();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
