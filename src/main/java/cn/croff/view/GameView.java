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

    private DotButton[][] board; // 放格子对应Button的棋盘
    private GameModel gameModel; // GameModel
    private JLabel stepsLabel; // 步数的Label
    private JLabel flagsAndMinesLabel; // 埋雷数和旗子数的Label

    /**
     * Constructor used for initializing the Frame
     *
     * @param gameModel      the model of the game (already initialized)
     * @param gameController the controller
     */
    public GameView(GameModel gameModel, GameController gameController) {
        // 初始化窗口标题、棋盘信息
        super("Minesweeper");
        this.gameModel = gameModel;
        int width = gameModel.getWidth();
        int heigth = gameModel.getHeigth();

        // 用一个BorderLayout作为root的JPanel
        JPanel root = new JPanel(new BorderLayout());

        // root的上面放埋雷数和旗子数的Label
        JPanel top = new JPanel();
        flagsAndMinesLabel = new JLabel();
        top.add(flagsAndMinesLabel);
        root.add(top, BorderLayout.NORTH);

        // root的下面放步数的Label、Reset按钮和Quit按钮
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

        // root的中间放棋盘
        JPanel content = new JPanel(new GridLayout(heigth, width));
        board = new DotButton[heigth][width];
        for (int i = 0; i < heigth; i++) {
            for (int j = 0; j < width; j++) {
                DotButton button = new DotButton(j, i, getIcon(j, i));
                button.addMouseListener(gameController);
                button.addActionListener(gameController);
                content.add(button);
                board[i][j] = button;
            }
        }
        root.add(content, BorderLayout.CENTER);

        // 把root设置为窗口内容
        setContentPane(root);
        // 设置窗口样式
        setDefaultLookAndFeelDecorated(true);
        // 设置退出按钮
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 设置窗口大小
        setSize(gameModel.getWidth() * 28, gameModel.getHeigth() * 28 + 90);
        // 设置窗口大小不可调整
        setResizable(false);
        // 设置窗口可见
        setVisible(true);
        // 更新界面内容
        update();
    }

    /**
     * update the status of the board's DotButton instances based on the current game model, then redraws the view.
     */
    public void update() {
        // 更新步数Label、Flag数Label和棋盘内容
        stepsLabel.setText("Number of steps: " + gameModel.getNumberOfSteps());
        flagsAndMinesLabel.setText("Number of mines: " + gameModel.getNumberOfMines() + " | Number of flags: " + gameModel.getNumberOfFlags());
        for (int i = 0; i < gameModel.getHeigth(); i++) {
            for (int j = 0; j < gameModel.getWidth(); j++) {
                board[i][j].setIconNumber(getIcon(j, i));
            }
        }
        // 刷新窗口内容
        this.repaint();
    }

    /**
     * returns the icon value that must be used for a given dot in the game
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the icon to use for the dot at location (i,j)
     */
    private int getIcon(int i, int j) {
        // 根据棋盘指定位置的格子状态，获取对应的图标数字
        if (gameModel.isFlag(i, j)) {
            // 先看是不是Flag
            return DotButton.FLAGGED;
        } else if (gameModel.isCovered(i, j)) {
            // 再看是不是Covered
            return DotButton.COVERED;
        } else if (gameModel.isMined(i, j)) {
            // 接着看是不是埋雷
            if (gameModel.hasBeenClicked(i, j)) {
                // 如果是埋雷了又点到了，就Boom
                return DotButton.CLICKED_MINE;
            } else {
                // 如果没点到就是一般雷
                return DotButton.MINED;
            }
        } else {
            // 如果以上都不是，就是没雷的格子，那就显示周围雷数的数字或Blank
            return gameModel.getNeighbooringMines(i, j);
        }
    }
}
