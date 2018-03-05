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
 * The class <b>cn.croff.controller.GameController</b> is the cn.croff.controller of the game. It is a listener
 * of the cn.croff.view, and has a method <b>play</b> which computes the next
 * step of the game, and  updates cn.croff.model and cn.croff.view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class GameController implements ActionListener, MouseListener {

    private GameView gameView;
    private GameModel gameModel;

    /**
     * Constructor used for initializing the cn.croff.controller. It creates the game's cn.croff.view
     * and the game's cn.croff.model instances
     *
     * @param width         the width of the board on which the game will be played
     * @param heigth        the heigth of the board on which the game will be played
     * @param numberOfMines the number of mines hidden in the board
     */
    public GameController(int width, int heigth, int numberOfMines) {
        // 初始化GameModel和GameView
        gameModel = new GameModel(width, heigth, numberOfMines);
        gameView = new GameView(gameModel, this);
    }

    /**
     * Callback used when the user clicks a button (reset or quit)
     *
     * @param e the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // 点击事件（左键）
        Object source = e.getSource();
        if (source.getClass().equals(DotButton.class)) {
            // 点击格子（踩雷）
            int x = ((DotButton) source).getColumn();
            int y = ((DotButton) source).getRow();
            play(x, y);
        } else if (source.getClass().equals(JButton.class)) {
            // 点击Reset按钮或Quit按钮
            if (((JButton) source).getText().equals("Reset")) {
                // 重置游戏
                reset();
            } else if (((JButton) source).getText().equals("Quit")) {
                // 退出游戏
                System.exit(0);
            }
        }
    }

    /**
     * resets the game
     */
    private void reset() {
        // GameModel重置之后更新界面
        gameModel.reset();
        gameView.update();
    }

    /**
     * <b>play</b> is the method called when the user clicks on a square.
     * If that square is not already clicked, then it applies the logic
     * of the game to uncover that square, and possibly end the game if
     * that square was mined, or possibly uncover some other squares.
     * It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives to options: start a new game, or exit
     *
     * @param width  the selected column
     * @param heigth the selected line
     */
    private void play(int width, int heigth) {
        // 格子是Covered的并且不是Flag的才能点
        if (gameModel.isCovered(width, heigth) && !gameModel.isFlag(width, heigth)) {
            // 增加步数，设置格子被点击和Uncover，更新界面
            gameModel.step();
            gameModel.click(width, heigth);
            gameModel.uncover(width, heigth);
            if (gameModel.isBlank(width, heigth)) {
                // 如果周围没雷，就要用clearZone方法清出一片没雷的区域
                clearZone(gameModel.get(width, heigth));
            }
            gameView.update();

            if (gameModel.isMined(width, heigth) || gameModel.isFinished()) {
                // 如果踩到雷或者胜利了就uncoverAll再更新界面，并且弹出对话框选择是否再玩一次
                gameModel.uncoverAll();
                gameView.update();
                String[] options = {"Play Again", "Quit"};
                int option;
                if (gameModel.isMined(width, heigth)) {
                    // 踩雷了弹出以下对话框
                    option = JOptionPane.showOptionDialog(null, "Aough, you lost in " + gameModel.getNumberOfSteps() + " steps!\nWould you like to play again?", "Boom!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                } else {
                    // 胜利了弹出以下对话框
                    option = JOptionPane.showOptionDialog(null, "Congratulations, you won in " + gameModel.getNumberOfSteps() + " steps!\nWould you like to play again?", "Won!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                }
                if (option == JOptionPane.YES_OPTION) {
                    // 如果选择了再玩一次，就重置游戏
                    reset();
                } else {
                    // 否则就退出游戏
                    System.exit(0);
                }
            }
        }
    }

    /**
     * <b>clearZone</b> is the method that computes which new dots should be ``uncovered''
     * when a new square with no mine in its neighborood has been selected
     *
     * @param initialDot the cn.croff.model.DotInfo object corresponding to the selected cn.croff.view.DotButton that
     *                   had zero neighbouring mines
     */
    private void clearZone(DotInfo initialDot) {
        // 用来清除一片没雷的区域
        // 先初始化一个栈，设置最大容量为棋盘的Size
        Stack<DotInfo> dotStack = new Stack<>();
        // 把初始格子push
        dotStack.push(initialDot);
        while (!dotStack.isEmpty()) {
            // 当栈不为空时，弹出一个格子
            DotInfo dot = dotStack.pop();
            int x = dot.getX();
            int y = dot.getY();
            // 把这个格子周围的格子全部uncover，如果其中有格子的周围没有雷（isBlank），就把这个格子push进栈
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
            if (x > 0 && y < gameModel.getHeigth() - 1 && gameModel.isCovered(x - 1, y + 1) && !gameModel.isMined(x - 1, y + 1)) {
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
            if (y < gameModel.getHeigth() - 1 && gameModel.isCovered(x, y + 1) && !gameModel.isMined(x, y + 1)) {
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
            if (x < gameModel.getWidth() - 1 && y < gameModel.getHeigth() - 1 && gameModel.isCovered(x + 1, y + 1) && !gameModel.isMined(x + 1, y + 1)) {
                gameModel.uncover(x + 1, y + 1);
                if (gameModel.isBlank(x + 1, y + 1)) {
                    dotStack.push(gameModel.get(x + 1, y + 1));
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 右键点击格子的时候是标记旗子
        if (e.getButton() == MouseEvent.BUTTON3) {
            Object source = e.getSource();
            int x = ((DotButton) source).getColumn();
            int y = ((DotButton) source).getRow();
            if (gameModel.isCovered(x, y) || gameModel.isFlag(x, y)) {
                // 当格子covered的时候才能标记旗子，当格子被标记的时候也可以取消标记
                gameModel.setFlag(x, y, !gameModel.isFlag(x, y));
                // 完成后更新界面
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
