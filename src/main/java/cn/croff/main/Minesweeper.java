package cn.croff.main;

import cn.croff.controller.GameController;

/**
 * The class <b>Mineseeper</b> launches the game
 */
public class Minesweeper {

    private static final int DEFAULT_WIDTH = 20; // 默认棋盘宽度
    private static final int DEFAULT_HEIGTH = 12; // 默认棋盘高度
    private static final int DEFAULT_MINES = 36; // 默认埋雷数

    /**
     * <b>main</b> of the application. Creates the instance of GameController and starts the game.
     * If three parameters width, heigth, number of mines are passed, they are used.
     * Otherwise, a default value is used.
     * Defaults values are also used if the paramters are too small (minimum 10 for width, 5 for heigth and 1 for number of mines).
     * Additionally, the maximum number of mines is capped at width*heigth -1
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        int width = DEFAULT_WIDTH;
        int heigth = DEFAULT_HEIGTH;
        int numberOfMines = DEFAULT_MINES;

        // 检测运行时传入参数（宽度、高度、埋雷数）
        if (args.length == 3) {
            try {
                width = Integer.parseInt(args[0]);
                if (width < 10) {
                    System.out.println("Invalid argument, using default...");
                    width = DEFAULT_WIDTH;
                }
                heigth = Integer.parseInt(args[1]);
                if (heigth < 5) {
                    System.out.println("Invalid argument, using default...");
                    heigth = DEFAULT_HEIGTH;
                }
                numberOfMines = Integer.parseInt(args[2]);
                if (numberOfMines < 1) {
                    System.out.println("Invalid argument, using default...");
                    numberOfMines = DEFAULT_MINES;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument, using default...");
                width = DEFAULT_WIDTH;
                heigth = DEFAULT_HEIGTH;
                numberOfMines = DEFAULT_MINES;
            }
        }
        // 如果埋雷数大于格子总数就提示错误，并把埋雷数设为格子总数-1
        if (numberOfMines >= width * heigth) {
            System.out.println("Too many mines: " + numberOfMines
                    + " mines on " + (width * heigth) + " spots. Using "
                    + (width * heigth - 1) + " instead. Good luck!");
            numberOfMines = (width * heigth - 1);
        }
        // 启动游戏
        new GameController(width, heigth, numberOfMines);
    }
}
