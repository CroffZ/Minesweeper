package cn.croff.model;

/**
 * The class <b>DotInfo</b> is a simple helper class to store the state
 * (e.g. clicked, mined, number of neighbouring mines...) at the dot position (x,y).
 */
public class DotInfo {

    private int x;
    private int y;
    private boolean mined;
    private boolean covered;
    private boolean wasClicked;
    private int neighbouringMines;
    private boolean flag;

    /**
     * Constructor used to initialize the instance variables.
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
     * Setter method for the attribute mined changing to true;
     */
    public void setMined() {
        mined = true;
    }

    /**
     * Getter method for the attribute mined.
     *
     * @return the value of the attribute mined
     */
    public boolean isMined() {
        return mined;
    }

    /**
     * Setter method for the attribute covered changing to false.
     */
    public void uncover() {
        covered = false;
    }

    /**
     * Getter method for the attribute covered.
     *
     * @return the value of the attribute covered
     */
    public boolean isCovered() {
        return covered;
    }

    /**
     * Setter method for the attribute wasClicked changing to true.
     */
    public void click() {
        wasClicked = true;
    }

    /**
     * Getter method for the attribute wasClicked.
     *
     * @return the value of the attribute wasClicked
     */
    public boolean hasBeenClicked() {
        return wasClicked;
    }

    /**
     * Getter method for the attribute neighbouringMines.
     *
     * @return neighbouringMines
     */
    public int getNeighbouringMines() {
        return neighbouringMines;
    }

    /**
     * Setter method for the attribute neighbouringMines.
     *
     * @param neighbouringMines new value of the attribute neighbouringMines
     */
    public void setNeighbouringMines(int neighbouringMines) {
        this.neighbouringMines = neighbouringMines;
    }

    /**
     * Getter method for the attribute flag.
     *
     * @return the value of the attribute
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * Setter method for the attribute flag.
     *
     * @param flag new value of the attribute flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
