package cn.croff.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * The class <b>cn.croff.model.GameModel</b> holds the cn.croff.model, the state of the systems.
 * It stores the following information:
 * - the state of all the ``dots'' on the board (mined or not, clicked
 * or not, number of neighbooring mines...)
 * - the size of the board
 * - the number of steps since the last reset
 * <p>
 * The cn.croff.model provides all of this informations to the other classes trough
 * appropriate Getters.
 * The cn.croff.controller can also update the cn.croff.model through Setters.
 * Finally, the cn.croff.model is also in charge of initializing the game
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class GameModel {

    private int widthOfGame; // 棋盘宽度
    private int heigthOfGame; // 棋盘高度
    private DotInfo[][] model; // 棋盘
    private int numberOfSteps; // 步数
    private int numberUncovered; // uncovered的格子数
    private int numberOfMines; // 埋雷数
    private int numberOfFlags; // flag的格子数

    /**
     * Constructor to initialize the cn.croff.model to a given size of board.
     *
     * @param width         the width of the board
     * @param heigth        the heigth of the board
     * @param numberOfMines the number of mines to hide in the board
     */
    public GameModel(int width, int heigth, int numberOfMines) {
        // 初始化棋盘，重置
        widthOfGame = width;
        heigthOfGame = heigth;
        model = new DotInfo[heigthOfGame][widthOfGame];
        this.numberOfMines = numberOfMines;
        reset();
    }

    /**
     * Resets the cn.croff.model to (re)start a game. The previous game (if there is one)
     * is cleared up .
     */
    public void reset() {
        // 重置步数、uncovered的格子数、flag的格子数
        numberOfSteps = 0;
        numberUncovered = heigthOfGame * widthOfGame;
        numberOfFlags = 0;
        Random generator = new Random();

        // 生成埋雷的数字集合
        // 每次生成一个从0到总格子数-1之间的数字放进Set，直到Set的元素数量等于埋雷数
        Set<Integer> minedSet = new HashSet<>();
        while (minedSet.size() < numberOfMines) {
            minedSet.add(generator.nextInt(numberUncovered));
        }

        // 从小到大取出埋雷的数字，从第一行开始，从左到右，一行结束后就到下一行
        // 如果行坐标+列坐标*棋盘宽度等于埋雷数字，则这个格子就埋雷，然后从Set里面删除这个埋雷数字
        for (int i = 0; i < heigthOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                model[i][j] = new DotInfo(j, i);
                if (minedSet.contains(i * widthOfGame + j)) {
                    minedSet.remove(i * widthOfGame + j);
                    model[i][j].setMined();
                }
            }
        }

        // 计算每个格子的周围埋雷数
        for (int i = 0; i < heigthOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                // 先把周围埋雷数初始化为0，然后对周围的八个格子（如果有）检测是否埋雷，有的话就+1
                int neighbooringMines = 0;

                if (i > 0) {
                    if (model[i - 1][j].isMined()) {
                        neighbooringMines++;
                    }
                    if (j > 0 && model[i - 1][j - 1].isMined()) {
                        neighbooringMines++;
                    }
                    if (j < widthOfGame - 1 && model[i - 1][j + 1].isMined()) {
                        neighbooringMines++;
                    }
                }

                if (j > 0 && model[i][j - 1].isMined()) {
                    neighbooringMines++;
                }
                if (j < widthOfGame - 1 && model[i][j + 1].isMined()) {
                    neighbooringMines++;
                }

                if (i < heigthOfGame - 1) {
                    if (model[i + 1][j].isMined()) {
                        neighbooringMines++;
                    }
                    if (j > 0 && model[i + 1][j - 1].isMined()) {
                        neighbooringMines++;
                    }
                    if (j < widthOfGame - 1 && model[i + 1][j + 1].isMined()) {
                        neighbooringMines++;
                    }
                }

                model[i][j].setNeighbooringMines(neighbooringMines);
            }
        }
    }

    /**
     * Getter method for the heigth of the game
     *
     * @return the value of the attribute heigthOfGame
     */
    public int getHeigth() {
        return heigthOfGame;
    }

    /**
     * Getter method for the width of the game
     *
     * @return the value of the attribute widthOfGame
     */
    public int getWidth() {
        return widthOfGame;
    }

    /**
     * returns true if the dot at location (i,j) is mined, false otherwise
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isMined(int i, int j) {
        return model[j][i].isMined();
    }

    /**
     * returns true if the dot  at location (i,j) has
     * been clicked, false otherwise
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean hasBeenClicked(int i, int j) {
        return model[j][i].hasBeenClicked();
    }

    /**
     * returns true if the dot  at location (i,j) has zero mined
     * neighboor, false otherwise
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isBlank(int i, int j) {
        return model[j][i].getNeighbooringMines() == 0;
    }

    /**
     * returns true if the dot is covered, false otherwise
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isCovered(int i, int j) {
        return model[j][i].isCovered();
    }

    /**
     * returns true if the dot is flag, false otherwise
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isFlag(int i, int j) {
        return model[j][i].isFlag();
    }

    /**
     * returns the number of neighbooring mines os the dot
     * at location (i,j)
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the number of neighbooring mines at location (i,j)
     */
    public int getNeighbooringMines(int i, int j) {
        return model[j][i].getNeighbooringMines();
    }

    /**
     * Sets the status of the dot at location (i,j) to uncovered
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     */
    public void uncover(int i, int j) {
        // uncover指定格子，同时uncover格子数量-1
        numberUncovered--;
        model[j][i].uncover();
    }

    /**
     * Sets the status of the dot at location (i,j) to clicked
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     */
    public void click(int i, int j) {
        model[j][i].click();
    }

    /**
     * Sets the flag of the dot at location (i,j)
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     */
    public void setFlag(int i, int j, boolean flag) {
        // 对指定格子标记旗子，同时更新标记旗子数量
        if (!model[j][i].isFlag() && flag) {
            numberOfFlags++;
        } else if (model[j][i].isFlag() && !flag) {
            numberOfFlags--;
        }
        model[j][i].setFlag(flag);
    }

    /**
     * Uncover all remaining covered dot
     */
    public void uncoverAll() {
        // uncover所有的格子，游戏结束时使用
        for (int i = 0; i < heigthOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                uncover(j, i);
            }
        }
    }

    /**
     * Getter method for the current number of steps
     *
     * @return the current number of steps
     */
    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    /**
     * Getter method for the number of mines
     *
     * @return the number of mines
     */
    public int getNumberOfMines() {
        return numberOfMines;
    }

    /**
     * Getter method for the current number of flags
     *
     * @return the current number of flags
     */
    public int getNumberOfFlags() {
        return numberOfFlags;
    }

    /**
     * Getter method for the cn.croff.model's dotInfo reference
     * at location (i,j)
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return cn.croff.model[i][j]
     */
    public DotInfo get(int i, int j) {
        return model[j][i];
    }

    /**
     * The metod <b>step</b> updates the number of steps. It must be called
     * once the cn.croff.model has been updated after the payer selected a new square.
     */
    public void step() {
        numberOfSteps++;
    }

    /**
     * The metod <b>isFinished</b> returns true iff the game is finished, that
     * is, all the nonmined dots are uncovered.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished() {
        return numberOfMines == numberUncovered;
    }

    /**
     * Builds a String representation of the cn.croff.model
     *
     * @return String representation of the cn.croff.model
     */
    public String toString() {
        // 把棋盘信息存在一个String中返回，主要用于打印棋盘信息到控制台来debug
        StringBuilder stringBuilder = new StringBuilder();
        // 打印带有Covered信息的棋盘
        stringBuilder.append("Covered Board: \n");
        for (int i = 0; i < heigthOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                if (isCovered(j, i)) {
                    stringBuilder.append("  ");
                } else {
                    if (isMined(j, i)) {
                        stringBuilder.append("B ");
                    } else {
                        stringBuilder.append(getNeighbooringMines(j, i)).append(" ");
                    }
                }
            }
            stringBuilder.append("\n");
        }
        // 打印没有Cover信息的棋盘
        stringBuilder.append("Uncovered Board: \n");
        for (int i = 0; i < heigthOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                if (isMined(j, i)) {
                    stringBuilder.append("B ");
                } else {
                    stringBuilder.append(getNeighbooringMines(j, i)).append(" ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
