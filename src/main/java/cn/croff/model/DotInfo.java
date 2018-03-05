package cn.croff.model;

/**
 * The class <b>cn.croff.model.DotInfo</b> is a simple helper class to store
 * the state (e.g. clicked, mined, number of neighbooring mines...)
 * at the dot position (x,y)
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class DotInfo {

    private int x; // 横坐标
    private int y; // 纵坐标
    private boolean mined; // 是否埋雷
    private boolean covered; // 内容是否被遮住
    private boolean wasClicked; // 是否被点击过
    private int neighbooringMines; // 周围的雷数
    private boolean flag; // 是否被标记旗子

    /**
     * Constructor, used to initialize the instance variables
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public DotInfo(int x, int y) {
        this.x = x;
        this.y = y;
        mined = false;
        covered = true;
        wasClicked = false;
        flag = false;
    }

    /**
     * Getter method for the attribute x.
     *
     * @return the value of the attribute x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter method for the attribute y.
     *
     * @return the value of the attribute y
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for mined
     */
    public void setMined() {
        mined = true;
    }

    /**
     * Getter for mined
     *
     * @return mined
     */
    public boolean isMined() {
        return mined;
    }

    /**
     * Setter for covered
     */
    public void uncover() {
        covered = false;
    }

    /**
     * Getter for covered
     *
     * @return covered
     */
    public boolean isCovered() {
        return covered;
    }

    /**
     * Setter for wasClicked
     */
    public void click() {
        wasClicked = true;
    }

    /**
     * Getter for wasClicked
     *
     * @return wasClicked
     */
    public boolean hasBeenClicked() {
        return wasClicked;
    }

    /**
     * Setter for neighbooringMines
     *
     * @param neighbooringMines number of neighbooring mines
     */
    public void setNeighbooringMines(int neighbooringMines) {
        this.neighbooringMines = neighbooringMines;
    }

    /**
     * Get for neighbooringMines
     *
     * @return neighbooringMines
     */
    public int getNeighbooringMines() {
        return neighbooringMines;
    }

    /**
     * Getter for flag
     *
     * @return flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * Setter for flag
     *
     * @param flag flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
